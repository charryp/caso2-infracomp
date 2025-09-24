import java.util.*;

public class Scheduler {
    private final MemoryManager mm;

    public Scheduler(MemoryManager mm) {
        this.mm = mm;
    }

    public void run(List<Process> procesos, int totalFrames) {
        if (procesos.isEmpty()) return;

        // Asignación equitativa inicial
        int n = procesos.size();
        int framesPerProc = totalFrames / n;

        // Asignar frames y loguear
        for (Process p : procesos) {
            List<Integer> got = mm.allocateFrames(framesPerProc, p.getPid());
            for (int f : got) {
                p.receiveNewFrame(f);
                System.out.println("Proceso " + p.getPid() + ": recibe marco " + f);
            }
        }

        // Cola RR
        Deque<Process> ready = new ArrayDeque<>();
        for (Process p : procesos) if (p.hasMore()) ready.addLast(p);

        while (!ready.isEmpty()) {
            Process p = ready.removeFirst();
            if (!p.hasMore()) continue; // seguridad

            int pid = p.getPid();
            System.out.println("Turno proc: " + pid);
            System.out.println("PROC " + pid + " analizando linea_: " + p.currentIndex());

            DVReference dv = p.peek();
            int vpn = dv.getVpn();

            Integer mappedFrame = p.getMappedFrameForVpn(vpn);
            if (mappedFrame != null) {
                // HIT
                p.onHit(mappedFrame);
                System.out.println("PROC " + pid + " hits: " + p.getHits());
                p.logAging(); // "envejecimiento"
                if (p.hasMore()) ready.addLast(p);
                else onProcessFinish(p, ready);
            } else {
                // FALLO de página
                boolean replaced = p.handlePageFault(vpn, mm, true);
                p.logAging();
                // NO avanzar índice; reintentar en siguiente turno
                ready.addLast(p);
            }
        }

        // Resumen final
        for (Process p : procesos) {
            int refs = p.getRefs().size();
            int faults = p.getFaults();
            int hits = p.getHits();
            int swaps = p.getSwaps();
            double failRate = refs == 0 ? 0 : ((double) faults) / refs;
            double hitRate = refs == 0 ? 0 : ((double) hits) / refs;

            System.out.println("Proceso: " + p.getPid());
            System.out.println("- Num referencias: " + refs);
            System.out.println("- Fallas: " + faults);
            System.out.println("- Hits: " + hits);
            System.out.println("- SWAP: " + swaps);
            System.out.printf("- Tasa fallas: %.4f%n", failRate);
            System.out.printf("- Tasa éxito: %.4f%n", hitRate);
        }
    }

    /** Cuando un proceso termina: libera marcos y reasigna al de más fallas en ejecución */
    private void onProcessFinish(Process finished, Deque<Process> ready) {
        System.out.println("========================  ");
        System.out.println("Termino proc: " + finished.getPid());
        System.out.println("======================== ");

        // Libera marcos del proceso terminado (al pool), log estilo anexo
        List<Integer> frames = finished.releaseAllFrames(mm);
        for (int f : frames) {
            System.out.println("PROC " + finished.getPid() + " removiendo marco: " + f);
            mm.freeFrameToPool(f);
        }
        if (ready.isEmpty()) return;

        // Elegir proceso en ejecución con mayor número de fallas
        Process target = null;
        int maxFaults = -1;
        for (Process p : ready) {
            if (p.getFaults() > maxFaults) {
                maxFaults = p.getFaults();
                target = p;
            }
        }
        if (target == null) return;

        // Reasignar tantos marcos como liberados
        List<Integer> newly = mm.allocateFrames(frames.size(), target.getPid());
        for (int f : newly) {
            target.receiveNewFrame(f);
            System.out.println("PROC " + target.getPid() + " asignando marco nuevo " + f);
        }
    }
}


import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class Scheduler {
    private final MemoryManager mm;

    public Scheduler(MemoryManager mm) {
        this.mm = mm;
    }

    public void run(List<Process> procesos, int totalFrames) {
        if (procesos.isEmpty()) return;

        int n = procesos.size();
        int framesPerProc = totalFrames / n;

        for (Process p : procesos) {
            List<Integer> got = mm.allocateFrames(framesPerProc, p.getPid());
            for (int f : got) {
                p.receiveNewFrame(f);
                System.out.println("Proceso " + p.getPid() + ": recibe marco " + f);
            }
        }

        Deque<Process> ready = new ArrayDeque<>();
        for (Process p : procesos) if (p.hasMore()) ready.addLast(p);

        while (!ready.isEmpty()) {
            Process p = ready.removeFirst();
            if (!p.hasMore()) continue;

            int pid = p.getPid();
            System.out.println("Turno proc: " + pid);
            System.out.println("PROC " + pid + " analizando linea_: " + p.currentIndex());

            DVReference dv = p.peek();
            int vpn = dv.getVpn();

            Integer mappedFrame = p.getMappedFrameForVpn(vpn);
            if (mappedFrame != null) {
                p.onHit(mappedFrame);
                System.out.println("PROC " + pid + " hits: " + p.getHits());
                p.logAging();
                if (p.hasMore()) ready.addLast(p);
                else onProcessFinish(p, ready);
            } else {
                boolean replaced = p.handlePageFault(vpn, mm, true);
                p.logAging();
                ready.addLast(p);
            }
        }

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

    private void onProcessFinish(Process finished, Deque<Process> ready) {
        System.out.println("========================  ");
        System.out.println("Termino proc: " + finished.getPid());
        System.out.println("======================== ");

        List<Integer> frames = finished.releaseAllFrames(mm);
        for (int f : frames) {
            System.out.println("PROC " + finished.getPid() + " removiendo marco: " + f);
            mm.freeFrameToPool(f);
        }
        if (ready.isEmpty()) return;

        Process target = null;
        int maxFaults = -1;
        for (Process p : ready) {
            if (p.getFaults() > maxFaults) {
                maxFaults = p.getFaults();
                target = p;
            }
        }
        if (target == null) return;

        List<Integer> newly = mm.allocateFrames(frames.size(), target.getPid());
        for (int f : newly) {
            target.receiveNewFrame(f);
            System.out.println("PROC " + target.getPid() + " asignando marco nuevo " + f);
        }
    }
}


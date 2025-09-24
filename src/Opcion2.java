import java.nio.file.*;
import java.util.*;

public class Opcion2 {

    public static void ejecutar(int totalFrames, int nprocs, Path carpeta) {
        System.out.println("Inicio:");

        MemoryManager mm = new MemoryManager(totalFrames);
        ReferenceLoader loader = new ReferenceLoader();

        List<Process> procesos = new ArrayList<>();
        for (int pid = 0; pid < nprocs; pid++) {
            Path f = carpeta.resolve("proc" + pid + ".txt");
            Process p = loader.loadFromFile(pid, f);
            procesos.add(p);
        }

        Scheduler sched = new Scheduler(mm);
        sched.run(procesos, totalFrames);
    }
}

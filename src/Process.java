import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Process {
    private final int pid;
    private final int tp;
    private final int nf, nc, nr, np;
    private final List<DVReference> refs;

    private final Map<Integer, Integer> vpnToFrame = new HashMap<>();

    private final List<Integer> assignedFrames = new ArrayList<>();

    private final Map<Integer, Long> lastUseByFrame = new HashMap<>();
    private long tick = 0L;

    private int hits = 0;
    private int faults = 0;
    private int swaps = 0;

    private int idx = 0;

    public Process(int pid, int tp, int nf, int nc, int nr, int np, List<DVReference> refs) {
        this.pid = pid;
        this.tp = tp;
        this.nf = nf;
        this.nc = nc;
        this.nr = nr;
        this.np = np;
        this.refs = refs;
    }

    public int getPid() { 
        return pid; 
    }
    public int getTp() {
         return tp; 
        }
    public int getNf() { 
        return nf; 
    }
    public int getNc() { 
        return nc; 
    }
    public int getNr() { 
        return nr; 
    }
    public int getNp() { 
        return np; 
    }
    public List<DVReference> getRefs() { 
        return refs; 
    }

    public boolean hasMore() { 
        return idx < refs.size(); 
    }
    public int currentIndex() { 
        return idx; 
    }
    public DVReference peek() { 
        return refs.get(idx); 
    }
    public void advance() { 
        idx++; 
    }

    public void receiveNewFrame(int frameId) {
        assignedFrames.add(frameId);
        lastUseByFrame.put(frameId, 0L);
    }

    public List<Integer> releaseAllFrames(MemoryManager mm) {
        List<Integer> out = new ArrayList<>(assignedFrames);
        for (int frameId : assignedFrames) {
            Integer vpn = mm.getFrame(frameId).getVpn();
            if (vpn != null) {
                vpnToFrame.remove(vpn);
            }
        }
        assignedFrames.clear();
        lastUseByFrame.clear();
        return out;
    }

    public List<Integer> getAssignedFrames() { 
        return assignedFrames; 
    }

    public Integer getMappedFrameForVpn(int vpn) {
        return vpnToFrame.get(vpn);
    }

    public void onHit(int frameId) {
        hits++;
        lastUseByFrame.put(frameId, ++tick);
        advance(); 
    }

    public boolean handlePageFault(int vpn, MemoryManager mm, boolean log) {
        faults++;
        Integer empty = mm.findOwnedEmptyFrame(pid, assignedFrames);
        if (empty != null) {
            vpnToFrame.put(vpn, empty);
            mm.mapPageInFrame(empty, pid, vpn);
            lastUseByFrame.put(empty, ++tick);
            swaps += 1;
            if (log) {
                System.out.println("PROC " + pid + " falla de pag: " + faults);
            }
            return false;
        } else {
            int victim = selectLRUFrame();
            Integer oldVpn = mm.getFrame(victim).getVpn();
            if (oldVpn != null) vpnToFrame.remove(oldVpn);

            vpnToFrame.put(vpn, victim);
            mm.mapPageInFrame(victim, pid, vpn);
            lastUseByFrame.put(victim, ++tick);
            swaps += 2;
            if (log) {
                System.out.println("PROC " + pid + " falla de pag: " + faults);
            }
            return true;
        }
    }

    private int selectLRUFrame() {
        long best = Long.MAX_VALUE;
        int bestId = assignedFrames.get(0);
        for (int f : assignedFrames) {
            long age = lastUseByFrame.getOrDefault(f, 0L);
            if (age < best) {
                best = age;
                bestId = f;
            }
        }
        return bestId;
    }

    public int getHits() { 
        return hits; 
    }
    public int getFaults() { 
        return faults; 
    }
    public int getSwaps() { 
        return swaps; 
    }
    public int getProcessedCount() { 
        return idx; 
    }

    public void logAging() {
        System.out.println("PROC " + pid + " envejecimiento");
    }
}

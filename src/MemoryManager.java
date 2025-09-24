import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;

public class MemoryManager {
    private final Frame[] frames;
    private final Deque<Integer> freeFrames = new ArrayDeque<>();

    public MemoryManager(int totalFrames) {
        this.frames = new Frame[totalFrames];
        for (int i = 0; i < totalFrames; i++) {
            frames[i] = new Frame(i);
            freeFrames.addLast(i);
        }
    }

    public int totalFrames() { 
        return frames.length; 
    }

    public Frame getFrame(int id) { 
        return frames[id]; 
    }

    public List<Integer> allocateFrames(int count, int ownerPid) {
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            if (freeFrames.isEmpty()) break;
            int id = freeFrames.removeFirst();
            Frame f = frames[id];
            f.setOwnerPid(ownerPid);
            f.setVpn(null);
            ids.add(id);
        }
        return ids;
    }

    public void freeFrameToPool(int frameId) {
        Frame f = frames[frameId];
        f.setOwnerPid(null);
        f.setVpn(null);
        freeFrames.addLast(frameId);
    }

    public void mapPageInFrame(int frameId, int pid, int vpn) {
        Frame f = frames[frameId];
        f.setOwnerPid(pid);
        f.setVpn(vpn);
    }

    public Integer findOwnedEmptyFrame(int pid, Collection<Integer> owned) {
        for (int id : owned) {
            Frame f = frames[id];
            if (f.isOwnedBy(pid) && f.getVpn() == null) 
                return id;
        }
        return null;
    }
}


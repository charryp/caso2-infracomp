public class Frame {
    private final int id;
    private Integer ownerPid; // null o pid
    private Integer vpn;      // vpn cargada o null

    public Frame(int id) {
        this.id = id;
        this.ownerPid = null;
        this.vpn = null;
    }

    public int getId() { return id; }
    public Integer getOwnerPid() { return ownerPid; }
    public Integer getVpn() { return vpn; }

    public void setOwnerPid(Integer ownerPid) { this.ownerPid = ownerPid; }
    public void setVpn(Integer vpn) { this.vpn = vpn; }

    public boolean isFree() { return ownerPid == null; }
    public boolean isOwnedBy(int pid) { return ownerPid != null && ownerPid == pid; }
    public boolean hasPageLoaded() { return vpn != null; }

    @Override
    public String toString() {
        return "Frame{" + id + ", owner=" + ownerPid + ", vpn=" + vpn + "}";
    }
}


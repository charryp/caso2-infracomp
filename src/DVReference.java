import java.util.Objects;

public class DVReference {
    private final String etiqueta; 
    private final int vpn;      
    private final int offset;    
    private final char op;        

    public DVReference(String etiqueta, int vpn, int offset, char op) {
        this.etiqueta = etiqueta;
        this.vpn = vpn;
        this.offset = offset;
        this.op = Character.toLowerCase(op);
    }

    public String getEtiqueta() { 
        return etiqueta; 
    }
    public int getVpn() { 
        return vpn; 
    }
    public int getOffset() { 
        return offset; 
    }
    public char getOp() { 
        return op; 
    }

    @Override
    public String toString() {
        return etiqueta + "," + vpn + "," + offset + "," + op;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DVReference)) 
            return false;
        DVReference d = (DVReference)o;
        return vpn == d.vpn && offset == d.offset && op == d.op &&
               Objects.equals(etiqueta, d.etiqueta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(etiqueta, vpn, offset, op);
    }
}


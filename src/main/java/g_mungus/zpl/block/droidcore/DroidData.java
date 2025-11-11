package g_mungus.zpl.block.droidcore;

import org.joml.Vector3dc;
import org.valkyrienskies.core.api.ships.Ship;

public class DroidData {
    public Ship targetShip;
    public Vector3dc targetPos;
    public double targetDist;
    public Vector3dc facingDirection;

    public DroidData() {
        this.targetShip = null;
        this.targetPos = null;
        this.targetDist = 0.0;
        this.facingDirection = null;
    }

    public boolean hasTarget() {
        return targetShip != null && targetPos != null;
    }

    public void setTarget(Ship ship, Vector3dc pos, double dist, Vector3dc facing) {
        this.targetShip = ship;
        this.targetPos = pos;
        this.targetDist = dist;
        this.facingDirection = facing;
    }

    public void clearTarget() {
        this.targetShip = null;
        this.targetPos = null;
        this.targetDist = 0.0;
        this.facingDirection = null;
    }
}

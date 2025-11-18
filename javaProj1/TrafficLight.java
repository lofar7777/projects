public class TrafficLight {
    public enum State { RED, GREEN, YELLOW }

    private State currentState;

    public TrafficLight() {
        currentState = State.RED;
    }

    public State getState() {
        return currentState;
    }

    public void nextState() {
        switch (currentState) {
            case RED: currentState = State.GREEN; break;
            case GREEN: currentState = State.YELLOW; break;
            case YELLOW: currentState = State.RED; break;
        }
    }
}

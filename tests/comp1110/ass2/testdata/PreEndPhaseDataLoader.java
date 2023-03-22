package comp1110.ass2.testdata;

public class PreEndPhaseDataLoader extends DataLoader<String> {
    public PreEndPhaseDataLoader(){
        super();
    }

    @Override
    protected String getFileName() {
        return "preEndPhase";
    }

    @Override
    protected String processLine(String line) {
        return line;
    }
}

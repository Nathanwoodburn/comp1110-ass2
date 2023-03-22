package comp1110.ass2.testdata;

public class IsPhaseOverDataLoader extends DataLoader<Boolean> {

    public IsPhaseOverDataLoader(){
        super();
    }

    protected String getFileName(){
        return "isPhaseOver";
    }

    @Override
    protected Boolean processLine(String line) {
        if(line.equals("true")){
            return true;
        }
        if(line.equals("false")){
            return false;
        }
        throw new RuntimeException("Invalid File Line. Got: " + line);
    }
}

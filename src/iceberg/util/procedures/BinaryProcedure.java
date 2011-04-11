package iceberg.util.procedures;

public interface BinaryProcedure<S,T> {
    public void execute(S object1, T object2);
}

package iceberg.util.procedures;

public interface UnaryProcedure<T> {
    public void execute(T object);
}

package info.meizi_retrofit.model;

/**
 * Created by Mr_Wrong on 16/1/24.
 */
public interface SaveDb<T> {
    void saveDb(T t);

    void saveDb(Iterable<T> objects);
}

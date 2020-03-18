package cn.wqz.sharding.java.dao;

import java.util.List;

/**
 * 数据访问接口
 * @param <T> Entity实体
 * @param <P> 某一列类型（对对应实体的某一属性的类型，一般为主键类型）
 */
public interface IBasicDao<T, P> {

    void createTableIfNotExists();

    void dropTable();

    void truncateTable();

    Long insert(T entity);

    List<T> select();

    void update(T entity);

    void delete(P key);
}

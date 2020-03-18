package cn.wqz.sharding.java.dao.impl;

import cn.wqz.sharding.java.dao.IOrderDao;
import cn.wqz.sharding.java.entity.Order;

import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class OrderDaoImpl implements IOrderDao {

    private final DataSource dataSource;

    public OrderDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void createTableIfNotExists() {
        // 创建sql
        String sql = "CREATE TABLE IF NOT EXISTS t_order "
                + "(order_id BIGINT NOT NULL AUTO_INCREMENT, user_id BIGINT NOT NULL, details VARCHAR(100), PRIMARY KEY (order_id))";
        // 获取连接
        try (Connection connection = dataSource.getConnection();
             // 获取执行句柄
             Statement statement = connection.createStatement()) {
            // 执行sql
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dropTable() {
        String sql = "DROP TABLE t_order";
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void truncateTable() {
        String sql = "TRUNCATE TABLE t_order";
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Long insert(Order order) {
        String sql = "INSERT INTO t_order (user_id, details) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, order.getUserId());
            preparedStatement.setString(2, order.getDetails());
            preparedStatement.executeUpdate();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    order.setOrderId(resultSet.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order.getOrderId();
    }

    @Override
    public List<Order> select() {
        String sql = "SELECT * FROM t_order";
        return listOrders(sql);
    }

    protected List<Order> listOrders(String sql) {
        List<Order> result = new LinkedList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Order order = new Order();
                order.setOrderId(resultSet.getLong(1));
                order.setUserId(resultSet.getLong(2));
                order.setDetails(resultSet.getString(3));
                result.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void update(Order order) {
        String sql = "UPDATE t_order SET user_id=?, details=? WHERE order_id=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, order.getUserId());
            preparedStatement.setString(2, order.getDetails());
            preparedStatement.setLong(3, order.getOrderId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM t_order WHERE order_id=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
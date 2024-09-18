package site.achun.tools.transfer.core;

import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Description
 * &#064;Author Heiffeng
 * &#064;Date 2024-09-18 13:43:30
 */
@RequiredArgsConstructor
public class CreateTableService {

    private final Connection connection;

    public boolean createTable(TableMappingInfo tableInfo) throws SQLException {
        String createTable = MySQLTableGenerator.generateCreateTableSQL(tableInfo);
        try (PreparedStatement stat = connection.prepareStatement(createTable)){
            return stat.execute();
        }
    }

}

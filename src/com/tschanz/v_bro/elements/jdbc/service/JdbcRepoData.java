package com.tschanz.v_bro.elements.jdbc.service;


/*public class JdbcRepoData implements RepoData {
    private final Logger logger = Logger.getLogger(JdbcRepoData.class.getName());
    private final JdbcConnectionFactory connectionFactory;
    private final JdbcQueryBuilder queryBuilder;


    public JdbcRepoData(
        JdbcConnectionFactory connectionFactory,
        JdbcQueryBuilder queryBuilder
    ) {
        this.connectionFactory = connectionFactory;
        this.queryBuilder = queryBuilder;
    }


    @Override
    public List<RowInfo> readData(RepoClass repoClass, List<RepoField> fields, List<RowFilter> rowFilters) throws RepoException {
        this.logger.info("reading rows from table " + repoClass.getName());

        ArrayList<RowInfo> rows = new ArrayList<>();
        try {
            String query = this.queryBuilder.buildQuery(repoClass.getName(), fields, rowFilters);
            Statement statement = this.connectionFactory.getCurrentConnection().createStatement();

            this.logger.info("executing query " + query);

            if (statement.execute(query)) {
                while (statement.getResultSet().next()) {
                    rows.add(this.parseRow(repoClass, fields, statement.getResultSet()));
                }
            }
        } catch (SQLException exception) {
            String msg = "error reading rows: " + exception.getMessage();
            this.logger.severe(msg);
            throw new RepoException(msg, exception);
        }

        return rows;
    }


    private RowInfo parseRow(RepoClass repoClass, List<RepoField> fields, ResultSet resultSet) throws SQLException {
        Map<String, FieldValue> fieldValueMap = new HashMap<>();

        for (RepoField field: fields) {
            fieldValueMap.put(
                field.getName(),
                this.parseFieldValue(repoClass, field, resultSet)
            );
        }

        return new RowInfo(fieldValueMap);
    }


    private FieldValue parseFieldValue(RepoClass repoClass, RepoField field, ResultSet resultSet) throws SQLException {
        switch (field.getType()) {
            case BOOL:
                return new FieldValue(repoClass, field, resultSet.getBoolean(this.queryBuilder.createFieldName(field)));
            case LONG:
                return new FieldValue(repoClass, field, resultSet.getLong(this.queryBuilder.createFieldName(field)));
            case DATE:
                return new FieldValue(repoClass, field, resultSet.getDate(this.queryBuilder.createFieldName(field)));
            case STRING:
            default:
                return new FieldValue(repoClass, field, resultSet.getString(this.queryBuilder.createFieldName(field)));
        }
    }
}
*/
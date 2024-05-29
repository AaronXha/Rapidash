package de.metanome.algorithms.dcfinder.input;

import com.csvreader.CsvReader;
import de.metanome.algorithms.dcfinder.helpers.IndexProvider;
import rapidash.Tuple;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Input {

    private final String name;
    private final int colCount;
    private final int rowCount;
    private final List<ParsedColumn<?>> parsedColumns;

    private final int[][] intInput;   // int expression of the dataset

    private final IndexProvider<String> providerS;
    private final IndexProvider<Long> providerL;
    private final IndexProvider<Double> providerD;


    public Input(RelationalInput relationalInput) {
        this(relationalInput, -1);
    }

    public Input(RelationalInput relationalInput, int rowLimit) {
        name = relationalInput.relationName();
        providerS = new IndexProvider<>();
        providerL = new IndexProvider<>();
        providerD = new IndexProvider<>();

        Column[] columns = readRelationalInputToColumns(relationalInput, rowLimit);
        colCount = columns.length;
        rowCount = colCount > 0 ? columns[0].getLineCount() : 0;

        parsedColumns = buildParsedColumns(columns);
        intInput = buildIntInput(parsedColumns);
    }

    private Column[] readRelationalInputToColumns(RelationalInput relationalInput, int rowLimit) {
        final int columnCount = relationalInput.numberOfColumns();
        Column[] columns = new Column[columnCount];
        for (int i = 0; i < columnCount; ++i)
            columns[i] = new Column(relationalInput.relationName(), relationalInput.columnNames[i]);

        int nLine = 0;
        try {
            CsvReader csvReader = new CsvReader(relationalInput.filePath, ',', StandardCharsets.UTF_8);
            csvReader.readHeaders();    // skip the header
            while (csvReader.readRecord()) {
                String[] line = csvReader.getValues();
                for (int i = 0; i < columnCount; ++i)
                    columns[i].addLine(line[i]);

                ++nLine;
                if (rowLimit > 0 && nLine >= rowLimit)
                    break;
            }
            csvReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return columns;
    }

    private Column[] readRelationalInputToColumns(RelationalInput relationalInput, int rowLimit,int start) {
        final int columnCount = relationalInput.numberOfColumns();
        Column[] columns = new Column[columnCount];
        for (int i = 0; i < columnCount; ++i)
            columns[i] = new Column(relationalInput.relationName(), relationalInput.columnNames[i]);

        int nLine = start;
        try {
            CsvReader csvReader = new CsvReader(relationalInput.filePath, ',', StandardCharsets.UTF_8);
            csvReader.readHeaders();    // skip the header
            while (csvReader.readRecord()) {
                String[] line = csvReader.getValues();
                for (int i = 0; i < columnCount; ++i)
                    columns[i].addLine(line[i]);

                ++nLine;
                if (rowLimit > 0 && nLine >= rowLimit)
                    break;
            }
            csvReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return columns;
    }

    private List<ParsedColumn<?>> buildParsedColumns(Column[] columns) {
        //存放每列的数据。
        List<ParsedColumn<?>> pColumns = new ArrayList<>(colCount);

        for (int i = 0; i < colCount; i++) {
            Column c = columns[i];
            if (c.getType() == Column.Type.LONG) {
                //name指代类型
                ParsedColumn<Long> pColumn = new ParsedColumn<>(c.getName(), Long.class, i, providerL);
                pColumns.add(pColumn);
                for (int l = 0; l < c.getLineCount(); ++l)
                    pColumn.addLine(c.getLong(l));
            } else if (c.getType() == Column.Type.NUMERIC) {
                ParsedColumn<Double> pColumn = new ParsedColumn<>(c.getName(), Double.class, i, providerD);
                pColumns.add(pColumn);
                for (int l = 0; l < c.getLineCount(); ++l)
                    pColumn.addLine(c.getDouble(l));
            } else if (c.getType() == Column.Type.STRING) {
                ParsedColumn<String> pColumn = new ParsedColumn<>(c.getName(), String.class, i, providerS);
                pColumns.add(pColumn);
                for (int l = 0; l < c.getLineCount(); ++l)
                    pColumn.addLine(c.getString(l));
            }
        }
        System.out.println(pColumns.size());
        return pColumns;
    }

    private int[][] buildIntInput(List<ParsedColumn<?>> pColumns) {
        //得到的是每一行每一列的索引值。这里得到的数据就已经将 具有相同大小的数据的索引变成一样的了。
        //将long和double重新进行排序
        IndexProvider.sort(providerL);
        IndexProvider.sort(providerD);

        int[][] currIntInput = new int[colCount][rowCount];

        for (int col = 0; col < colCount; col++) {
            ParsedColumn<?> pColumn = pColumns.get(col);
            for (int row = 0; row < rowCount; ++row)
                currIntInput[col][row] = pColumn.getIndexAt(row);
        }

        return currIntInput;
    }


    public long getRowCount() {
        return rowCount;
    }

    public int[][] getIntInput() {
        return intInput;
    }

    public ParsedColumn<?>[] getColumns() {
        return parsedColumns.toArray(new ParsedColumn[0]);
    }

    public List<ParsedColumn<?>> getParsedColumns() {
        return parsedColumns;
    }

    public String getName() {
        return name;
    }

    public List<Tuple> getTuples() {
        List<Tuple> tuples = new ArrayList<>();
        List<String> columns = new ArrayList<>();
        for(ParsedColumn<?> parsedColumn: parsedColumns){
            columns.add(parsedColumn.getColumnName());
        }
        for(int i = 0; i < rowCount; i++){
            List<Integer> values = new ArrayList<>();
            Map<String, Integer> map = new HashMap<>();
            for(int j = 0; j < colCount; j++){
                String col = parsedColumns.get(j).getColumnName();
                Integer value = intInput[i][j];
                values.add(value);
                map.put(col, value);
            }
            tuples.add(new Tuple(columns, values, map));
        }
        return tuples;
    }

}

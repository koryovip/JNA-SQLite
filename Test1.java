package sqlite3;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

import sqlite3.SQLite.sqlite3_exec_callback_callback;

public class Test1 {

    public static void main(String[] args) {
        SQLite sqlite = SQLite.INSTANCE;
        System.out.println(sqlite.sqlite3_libversion());
        System.out.println(sqlite.sqlite3_sourceid());
        System.out.println(sqlite.sqlite3_libversion_number());

        final PointerByReference db = new PointerByReference();
        int ret = sqlite.sqlite3_open("r:/1.db", db);
        System.out.println(ret);
        // System.out.println(conn);

        PointerByReference errmsg = new PointerByReference();
        /*
        ret = sqlite.sqlite3_exec(conn.getValue(), "DROP TABLE IF EXISTS tbl1", null, null, errmsg);
        System.out.println(ret);

        ret = sqlite.sqlite3_exec(conn.getValue(), "create table tbl1 (first_name TEXT NOT NULL, second_name TEXT NOT NULL)", null, null, errmsg);
        System.out.println(ret);
        */

        ret = sqlite.sqlite3_exec(db.getValue() //
        , "PRAGMA journal_mode = WAL", null, null, errmsg);
        System.out.println(ret);

        ret = sqlite.sqlite3_exec(db.getValue() //
        , "begin", null, null, errmsg);
        System.out.println(ret);

        ret = sqlite.sqlite3_exec(db.getValue() //
        , "insert into tbl1 (first_name,second_name) values ('aaa','ccc')", null, null, errmsg);
        System.out.println(ret);
        if (ret != 0) {
            System.out.println(errmsg.getValue().getString(0));
        }

        ret = sqlite.sqlite3_exec(db.getValue() //
        , "rollback", null, null, errmsg);
        System.out.println(ret);

        ret = sqlite.sqlite3_exec(db.getValue() //
        , "select * from tbl1 order by second_name desc" //
                , new sqlite3_exec_callback_callback() {

                    private boolean headerOutPut = false;

                    @Override
                    public int apply(Pointer voidPtr1, int col_cnt, String[] row_txt, String[] col_name) {
                        //System.out.println(col_cnt);
                        if (!headerOutPut) {
                            System.out.println(String.format("%s\t%s", col_name[0], col_name[1]));
                            headerOutPut = true;
                        }
                        System.out.println(String.format("%s\t%s", row_txt[0], row_txt[1]));
                        return 0;
                    }
                }, null, errmsg);
                // System.out.println(errmsg.getPointer());

        // ---------------------------
        System.out.println("---------------");
        final PointerByReference stmt = new PointerByReference();
        ret = sqlite.sqlite3_prepare_v2(db.getValue() //
        , "insert into tbl1 (first_name,second_name) values (?,?)" //
                , 128, stmt, null);
        System.out.println(ret);

        ret = sqlite.sqlite3_bind_text(stmt.getValue(), 1, "日本語", 12, SQLite.SQLITE_TRANSIENT);
        System.out.println(ret);

        ret = sqlite.sqlite3_bind_text(stmt.getValue(), 2, "大丈夫？", 16, SQLite.SQLITE_TRANSIENT);
        System.out.println(ret);

        while (SQLite.SQLITE_DONE != sqlite.sqlite3_step(stmt.getValue())) {

        }

        ret = sqlite.sqlite3_finalize(stmt.getValue());
        System.out.println(ret);
        System.out.println("---------------");

        ret = sqlite.sqlite3_close(db.getValue());
        System.out.println(ret);
        ret = sqlite.sqlite3_free(errmsg.getValue());
        System.out.println(ret);
    }

}

package sqlite3;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

public interface SQLite extends Library {

    public static final int SQLITE_OK = 0;
    public static final int SQLITE_DONE = 101;

    public static final int SQLITE_STATIC = 0;
    public static final int SQLITE_TRANSIENT = -1;

    SQLite INSTANCE = (SQLite) Native.loadLibrary("sqlite3", SQLite.class);

    public String sqlite3_libversion();

    public String sqlite3_sourceid();

    public int sqlite3_libversion_number();

    public int sqlite3_open(String fileName, PointerByReference conn);

    public int sqlite3_exec(Pointer conn, String sql, sqlite3_exec_callback_callback callback, Pointer voidPtr1, PointerByReference errmsg);

    // -----------
    public int sqlite3_prepare_v2(Pointer conn, String sql, int bByte, PointerByReference ppStmt, PointerByReference pzTail);

    public int sqlite3_bind_int(Pointer stmt, int idx, int value);

    public int sqlite3_bind_text(Pointer stmt, int idx, String value, int length, int sqlite3_destructor_type);

    public int sqlite3_bind_parameter_count(Pointer stmt);

    public int sqlite3_step(Pointer stmt);

    public int sqlite3_finalize(Pointer stmt);
    // -----------

    public int sqlite3_close(Pointer conn);

    public int sqlite3_free(Pointer msg);

    public interface sqlite3_exec_callback_callback extends Callback {
        int apply(Pointer voidPtr1, int int1, String[] row_txt, String[] col_name);
    }
}

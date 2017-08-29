oracle Instant Client
http://www.oracle.com/technetwork/topics/winx64soft-089540.html

Instant Client のインストール
http://apis.jpn.ph/fswiki/wiki.cgi?page=Oracle%2FInstantClient

OSqlEdit Oracle SQLエディタ (開発・運用支援ツール)
http://www.hi-ho.ne.jp/a_ogawa/osqledit/


alter table EMP move tablespace users;

select /*+ INDEX(EMP IDX_01) */ * from EMP where emp_id='111' and EMP_NAME='443' 
Error
[row:7,col:1] ORA-01502: 索引'RIVUS.IDX_01'またはそのパーティションが使用不可の状態です。

ALTER INDEX PK_EMP_ID REBUILD NOLOGGING;
ALTER INDEX IDX_01 REBUILD NOLOGGING;

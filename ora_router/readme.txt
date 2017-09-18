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


------------------------------------------------------------------
http://qiita.com/jsaito/items/33c7b2b5dd80a7447a14
Oracle 11g XEのDBをShift JIS化する

root@localhost # su - oracle
oracle@localhost $ sqlplus / as sysdba
SQL> select value from nls_database_parameters where parameter='NLS_CHARACTERSET';
SQL> shutdown immediate
SQL> startup restrict mount
SQL> drop database;
SQL> quit

oracle@localhost $ createdb.sh -dbchar JA16SJIS

dockerの場合、以下の処理が必要
cd data
mv product _product
cp -r ../share/oracle-product product
------------------------------------------------------------------
pfileとSPFILE、どちらを使用しているのかを確認する！
http://at-j.co.jp/blog/?p=5468


------------------------------------------------------------------
SQL> sqlplus / as sysdba
接続されました。
 
SQL> CREATE USER rivus
IDENTIFIED BY "RIVUS_PASS"
DEFAULT TABLESPACE users
TEMPORARY TABLESPACE temp
/
 
ユーザーが作成されました。
好き勝手に動かしてもよいテスト環境であれば、DBA ロールの付与と表領域の使用量を無制限にしておくと良い。
SQL> GRANT DBA TO RIVUS ;
 
権限付与が成功しました。
 
SQL> GRANT UNLIMITED TABLESPACE TO RIVUS ;
 
権限付与が成功しました。

-----------------------------------
Oracle で日本語を取り扱うときの文字コード
https://www.shift-the-oracle.com/config/multibyte-characterset.html
SELECT PARAMETER, VALUE
  FROM NLS_DATABASE_PARAMETERS
 WHERE PARAMETER IN ('NLS_CHARACTERSET', 'NLS_NCHAR_CHARACTERSET');


-----------------------------------
如何让docker容器和宿主机在一个网段，并组成局域网
docker network create -d macvlan --subnet=192.168.1.0/24 --gateway=192.168.1.1 -o parent=eth0 mynet
docker pull alpine
docker run --restart=always --net=mynet --name="test1" --ip=192.168.1.100 --privileged=true -dit 76da55c8019d /bin/ash
docker run --restart=always --net=mynet --name="test2" --ip=192.168.1.101 --privileged=true -dit 76da55c8019d /bin/ash
docker exec -it test1 /bin/ash
docker exec -it test2 /bin/ash

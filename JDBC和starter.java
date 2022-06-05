1.JDBC

1.1基本步骤：

​		加载驱动类：Class.forName("com.mysql.jdbc.Driver");

​		获得数据库连接：Connection conn = Drivermanager.get(url,user,password);

​		操作数据库：preparedStatement pst = coon.preparedStatement(sql);

​		ResultSet rs=pst.executeQuery();
1.2增删改查操作
//通用的增删改操作
    public void update(String sql,Object ...args) throws Exception {//sql中占位符的个数与可变形参的长度相同

        //1、获取数据库的连接
        Connection conn = JDBCUtils.getConnection();
        //2、预编译sql语句，返回PreparedStatement的实例
        PreparedStatement ps = conn.prepareStatement(sql);
        //3、填充占位符
        for (int i=0;i<args.length;i++){
            ps.setObject(i+1,args[i]);
        }
        //4、执行
        ps.execute();

        //5、资源关闭
        JDBCUtils.closeResource(conn,ps);


    }
//查
//返回集合
    public <T> List<T> getForList(Class<T> clazz, String sql, Object ...args){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs= null;
        ArrayList<T> list = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i=0;i<args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            //结果本身在ResultSet中，修饰结果的列名、列列数之类的在元数据中
            rs = ps.executeQuery();
            //获取结果集的元数据：ResultSetMetaData
            ResultSetMetaData rsmd = rs.getMetaData();
            //System.out.println(rsmd);
            //通过ResultSetMetaData获取结果集中的列数
            int columnCount = rsmd.getColumnCount();
            //创建集合对象
            list = new ArrayList<>();
            while (rs.next()){
                T t = clazz.newInstance();

                //处理结果集一行数据中的每列：给t对象指定的属性赋值
                for (int i=0;i<columnCount;i++){
                    Object columValue = rs.getObject(i + 1);
                    //获取每个列的列名
                    //String columName=rsmd.getColumnName(i+1);
                    //获取列的别名(无别名，使用列名)
                    String columName=rsmd.getColumnLabel(i+1);
                    //给指定对象指定的columName属性，赋值为columValue，通过反射
                    Field field = clazz.getDeclaredField(columName);
                    field.setAccessible(true);
                    field.set(t,columValue);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,ps,rs);
        }
        return null;
    }

//starter的实现
引入对应的依赖
编写实现类
编写配置文件读取类 主要注解是@ConfigruationProperties(“配置的值例如 example.a”)
编写自动装配类
编写默认的配置文件
在resourcesMETA-INFspring.factories 中配置我们的自动装配类


package com.creepersan.bingimage.lib.database.sqlite

import com.creepersan.bingimage.lib.database.bean.BingImageDatabaseBean
import com.creepersan.bingimage.lib.database.factory.DatabaseOperator
import com.creepersan.bingimage.lib.file.FileUtils
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.util.ArrayList

class SQLiteDatabaseOperator : DatabaseOperator() {

    init {
        Class.forName("org.sqlite.JDBC");
    }

    fun getConnection(): Connection {
        return DriverManager.getConnection("jdbc:sqlite:${FileUtils.sqliteFolder.absolutePath}/bing_image.db")
    }

    /**
     * 初始化表
     */
    override fun initTable() {
        val connection = getConnection()
        val statement = connection.createStatement()
        statement.queryTimeout = 30

        // 创建表
        statement.executeUpdate("create table if not exists BingImage ( " +
                "_id int not null primary key unique , " +
                "year int not null , " +
                "month int not null , " +
                "day int not null , " +
                "copyright varchar(1024) not null , " +
                "author varchar(1024) not null , " +
                "path varchar(1024) not null , " +
                "title varchar(1024) not null , " +
                "location varchar(1024) not null , " +
                "json text character not null " +
                ")")

        connection.close()
    }

    /**
     * 插入或者更新必应壁纸
     */
    override fun insertOrUpdateBingImage(bingImage: BingImageDatabaseBean) {
        val connection = getConnection()

        // 更新或插入数据
        val prepareStatement = connection.prepareStatement("replace into BingImage(_id, year, month, day, copyright, author, path, title, location, json) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")
        prepareStatement.setInt(1, bingImage._id)
        prepareStatement.setInt(2, bingImage.year)
        prepareStatement.setInt(3, bingImage.month)
        prepareStatement.setInt(4, bingImage.day)
        prepareStatement.setString(5, bingImage.copyright)
        prepareStatement.setString(6, bingImage.author)
        prepareStatement.setString(7, bingImage.path)
        prepareStatement.setString(8, bingImage.title)
        prepareStatement.setString(9, bingImage.location)
        prepareStatement.setString(10, bingImage.json)
        prepareStatement.execute()


        connection.close()
    }

    override fun removeBingImage(id: Int) {
        val connection = getConnection()

        // 更新或插入数据
        val prepareStatement = connection.prepareStatement("delete from BingImage where _id = ?")
        prepareStatement.setInt(1, id)

        connection.close()
    }

    /**
     * 获取指定数量的必应图片
     */
    override fun getBingImage(count: Int, index: Int): List<BingImageDatabaseBean> {
        val connection = getConnection()

        val prepareStatement = connection.prepareStatement("select * from BingImage order by _id desc limit ? offset ?")
        prepareStatement.setInt(1, count)
        prepareStatement.setInt(2, index)

        val resultSet : ResultSet
        try {
            resultSet = prepareStatement.executeQuery()
        } catch (e: Exception) {
            return listOf()
        }
        val list = ArrayList<BingImageDatabaseBean>()
        while (resultSet.next()){
            val bean = BingImageDatabaseBean(
                    _id = resultSet.getInt("_id"),
                    year = resultSet.getInt("year"),
                    month = resultSet.getInt("month"),
                    day = resultSet.getInt("day"),
                    copyright = resultSet.getString("copyright"),
                    author = resultSet.getString("author"),
                    path = resultSet.getString("path"),
                    title = resultSet.getString("title"),
                    location = resultSet.getString("location"),
                    json = resultSet.getString("json")
            )
            list.add(bean)
        }

        connection.close()

        return list
    }

    /**
     * 返回所有图片的数量
     */
    override fun getBingImageCount(): Int {
        val connection = getConnection()

        val prepareStatement = connection.createStatement()

        val result = prepareStatement.executeQuery("select count(*) from BingImage")

        result.next()
        val row = result.getInt(1)

        connection.close()

        return row
    }

    /**
     * 返回随机数量的图片
     */
    override fun getBingImageRandom(count: Int): List<BingImageDatabaseBean> {
        val connection = getConnection()

        val prepareStatement = connection.prepareStatement("select * from BingImage order by random() limit ?")
        prepareStatement.setInt(1, count)

        val resultSet : ResultSet
        try {
            resultSet = prepareStatement.executeQuery()
        } catch (e: Exception) {
            return listOf()
        }
        val list = ArrayList<BingImageDatabaseBean>()
        while (resultSet.next()){
            val bean = BingImageDatabaseBean(
                    _id = resultSet.getInt("_id"),
                    year = resultSet.getInt("year"),
                    month = resultSet.getInt("month"),
                    day = resultSet.getInt("day"),
                    copyright = resultSet.getString("copyright"),
                    author = resultSet.getString("author"),
                    path = resultSet.getString("path"),
                    title = resultSet.getString("title"),
                    location = resultSet.getString("location"),
                    json = resultSet.getString("json")
            )
            list.add(bean)
        }

        connection.close()

        return list
    }

}
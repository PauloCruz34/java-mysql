package com.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


public class CSVconsume{

    /**
     * @param args
     */
    public static void main(String[] args) {
        String jdbcUrl="jdbc:mysql://localhost:3306/gerenciamentotg";
        String username="root";
        String password="109081";

        String filePath="banco\\demo\\src\\trabalhodeGraduacao-202302.csv";

        int batchSize=20;

        Connection connection=null;


        try{
            connection= DriverManager.getConnection(jdbcUrl,username,password);
            connection.setAutoCommit(false);

            String sql="insert into Aluno(emailFatec,emailFornecido,nome) values(?,?,?)";

            PreparedStatement statement=connection.prepareStatement(sql);

            BufferedReader lineReader=new BufferedReader(new FileReader(filePath));

            String lineText=null;
            int count=0;

            lineReader.readLine();
            while ((lineText=lineReader.readLine())!=null){
                String[] data=lineText.split(",");

                String emailFatec=data[1];
                String emailFornecido=data[2];
                String nome=data[3];


                
                statement.setString(1,emailFatec);
                statement.setString(2,emailFornecido);
                statement.setString(3,nome);
                statement.addBatch();
                if(count%batchSize==0){
                    statement.executeBatch();
                }
            }
            lineReader.close();
            statement.executeBatch();
            connection.commit();
            connection.close();
            System.out.println("Data has been inserted successfully.");

        }
        catch (Exception exception){
            exception.printStackTrace();
        }

    }
}
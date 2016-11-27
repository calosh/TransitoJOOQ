package ec.edu.utpl.dcce.sic.arqapp.transitojooq.transitojooq;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.*;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.impl.DSL;
import static org.jooq.util.maven.example.tables.Provincias.PROVINCIAS;
import static org.jooq.util.maven.example.tables.Vehiculos.VEHICULOS;

public class Main {
    public static void main(String[] args) {
        String userName = "postgres";
        String password = "utpl";
        String url = "jdbc:postgresql://localhost/base_transito_jooq";

        // Connection is the only JDBC resource that we need
        // PreparedStatement and ResultSet are handled by jOOQ, internally
        try (Connection conn = DriverManager.getConnection(url, userName, password)) {
            
            DSLContext create = DSL.using(conn, SQLDialect.POSTGRES);
            
            // SELECT
            Result<Record> result = create.select().from(VEHICULOS).where(VEHICULOS.MARCA.equal("HYUNDAI")).fetch();
          
            for (Record r : result) {
                Integer id = r.getValue(VEHICULOS.IDCLASEV);
                String placa = r.getValue(VEHICULOS.PLACA);
                String marca = r.getValue(VEHICULOS.MARCA);

                System.out.println("ID: " + id + " Placa: " + placa + " Marca: " + marca);
            }
            
            Result<Record> result2 = create.select().from(VEHICULOS).fetch();
            
            System.out.println(result2.stream()
            .filter(a -> a.getValue(VEHICULOS.MARCA).contains("CHEVROLET"))
            .findFirst());
            
            
            
            // INSERT
            create.insertInto(PROVINCIAS,
                PROVINCIAS.NOMBREPROV)
                .values("CHIMBORAZO")
                .execute();
            
            
            create.insertInto(VEHICULOS,
                VEHICULOS.PLACA, VEHICULOS.MODELO,VEHICULOS.MARCA,VEHICULOS.TONELAJE,VEHICULOS.ASIENTOS,
                VEHICULOS.IDTIPOV, VEHICULOS.IDCLASEV,VEHICULOS.IDCOMB,VEHICULOS.IDPROV)
                .values("UJR-8742", "2010", "HYUNDAI", 800, 3, 4, 2, 1, 1)
                .execute();
            
            
            // UPDATE
            create.update(PROVINCIAS)
                    .set(PROVINCIAS.NOMBREPROV, "Cañar")
                    .where(PROVINCIAS.IDPROV.equal(2))
                    .execute();
        
            

            // DELETE
            create.delete(VEHICULOS)
                    .where(VEHICULOS.IDVEHICULO.equal(4145))
                    .execute();

            
            
        }
       
        // For the sake of this tutorial, let's keep exception handling simple
        catch (Exception e) {
            e.printStackTrace();
        }   
        
    }
}
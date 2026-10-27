package com.exemplo.tarefasapp.dados

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
// imports

@Database(entities = [Tarefa::class, Categoria::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tarefaDao(): TarefaDao
    abstract fun categoriaDao(): CategoriaDao
// declara o banco

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
//singleton - volatile para garantir que não venha valor antigo na thread(vem valor na hora)

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tarefas_app.db"
                ).addCallback(DadosIniciais).build()
                INSTANCE = instancia
                instancia
            }
        }
    }  // pega ou cria o banco
}


private object DadosIniciais : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        db.execSQL("INSERT INTO categorias (nome, cor) VALUES ('Faculdade', ${0xFF6750A4})")
        db.execSQL("INSERT INTO categorias (nome, cor) VALUES ('Casa', ${0xFF4CAF50})")
        db.execSQL("INSERT INTO categorias (nome, cor) VALUES ('Pessoal', ${0xFFEF6C00})")
        db.execSQL("INSERT INTO tarefas (titulo, descricao, categoriaId, concluida, prazoDias) VALUES ('Entregar trabalho de DAM', 'Terminar o app de tarefas e categorias', 1, 0, 3)")
        db.execSQL("INSERT INTO tarefas (titulo, descricao, categoriaId, concluida, prazoDias) VALUES ('Estudar para a prova', 'Revisar o conteúdo de Room', 1, 0, 7)")
        db.execSQL("INSERT INTO tarefas (titulo, descricao, categoriaId, concluida, prazoDias) VALUES ('Lavar roupa', 'Colocar a maquina para lavar antes do meio dia', 2, 1, 0)")
        db.execSQL("INSERT INTO tarefas (titulo, descricao, categoriaId, concluida, prazoDias) VALUES ('Ir na academia', 'Treino de pernas', 3, 0, 1)")
    }
}
// Roda so na primeira vez que o banco e criado, para o app nao abrir vazio
//mocks!
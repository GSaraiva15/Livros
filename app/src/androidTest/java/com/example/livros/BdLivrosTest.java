package com.example.livros;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class BdLivrosTest {
    @Before
    public void apagaBaseDados() {
        getTargetContext().deleteDatabase(BdLivrosOpenHelper.NOME_BASE_DADOS);
    }

    @Test
    public void consegueAbrirBaseDados() {
        // Context of the app under test.
        Context appContext = getTargetContext();

        BdLivrosOpenHelper openHelper = new BdLivrosOpenHelper(appContext);
        SQLiteDatabase bdLivros = openHelper.getReadableDatabase();
        assertTrue(bdLivros.isOpen());
        bdLivros.close();
    }

    private Context getTargetContext() {
        return InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    private long insereCategoria(BdTableCategorias tabelaCategorias, Categoria categoria) {
        long id = tabelaCategorias.insert(Converte.categoriaToContentValues(categoria));
        assertNotEquals(-1, id);

        return id;
    }

    private long insereCategoria(BdTableCategorias tabelaCategorias, String descricao) {
        Categoria categoria = new Categoria();
        categoria.setDescricao(descricao);

        return insereCategoria(tabelaCategorias, categoria);
    }

    private long insereLivro(SQLiteDatabase bdLivros, String titulo, String descCategoria) {
        BdTableCategorias tabelaCategorias = new BdTableCategorias(bdLivros);

        long idCategoria = insereCategoria(tabelaCategorias, descCategoria);

        BdTableLivros tabelaLivros = new BdTableLivros(bdLivros);

        Livro livro = new Livro();
        livro.setTitulo(titulo);
        livro.setIdCategoria(idCategoria);
        long idLivro = tabelaLivros.insert(Converte.livroToContentValues(livro));
        assertNotEquals(-1, idLivro);

        return idLivro;
    }

    @Test
    public void consegueInserirCategorias() {
        Context appContext = getTargetContext();

        BdLivrosOpenHelper openHelper = new BdLivrosOpenHelper(appContext);
        SQLiteDatabase bdLivros = openHelper.getWritableDatabase();

        BdTableCategorias tabelaCategorias = new BdTableCategorias(bdLivros);

        insereCategoria(tabelaCategorias, "Ação");

        bdLivros.close();
    }

    @Test
    public void consegueLerCategorias() {
        Context appContext = getTargetContext();

        BdLivrosOpenHelper openHelper = new BdLivrosOpenHelper(appContext);
        SQLiteDatabase bdLivros = openHelper.getWritableDatabase();

        BdTableCategorias tabelaCategorias = new BdTableCategorias(bdLivros);
        Cursor cursor = tabelaCategorias.query(BdTableCategorias.TODOS_CAMPOS, null, null, null, null, null);
        int registos = cursor.getCount();
        cursor.close();

        insereCategoria(tabelaCategorias, "Sci-fi");

        cursor = tabelaCategorias.query(BdTableCategorias.TODOS_CAMPOS, null, null, null, null, null);
        assertEquals(registos + 1, cursor.getCount());
        cursor.close();

        bdLivros.close();
    }

    @Test
    public void consegueAlterarCategorias() {
        Context appContext = getTargetContext();

        BdLivrosOpenHelper openHelper = new BdLivrosOpenHelper(appContext);
        SQLiteDatabase bdLivros = openHelper.getWritableDatabase();

        BdTableCategorias tabelaCategorias = new BdTableCategorias(bdLivros);

        Categoria categoria = new Categoria();
        categoria.setDescricao("Romanc");

        long id = insereCategoria(tabelaCategorias, categoria);

        categoria.setDescricao("Romance");
        int registosAlterados = tabelaCategorias.update(Converte.categoriaToContentValues(categoria), BdTableCategorias._ID + "=?", new String[]{String.valueOf(id)});
        assertEquals(1, registosAlterados);

        bdLivros.close();
    }

    @Test
    public void consegueApagarCategorias() {
        Context appContext = getTargetContext();

        BdLivrosOpenHelper openHelper = new BdLivrosOpenHelper(appContext);
        SQLiteDatabase bdLivros = openHelper.getWritableDatabase();

        BdTableCategorias tabelaCategorias = new BdTableCategorias(bdLivros);

        long id = insereCategoria(tabelaCategorias, "TESTE");

        int registosApagados = tabelaCategorias.delete(BdTableCategorias._ID + "=?", new String[]{String.valueOf(id)});
        assertEquals(1, registosApagados);

        bdLivros.close();
    }

    @Test
    public void consegueInserirLivros() {
        Context appContext = getTargetContext();

        BdLivrosOpenHelper openHelper = new BdLivrosOpenHelper(appContext);
        SQLiteDatabase bdLivros = openHelper.getWritableDatabase();

        insereLivro(bdLivros, "O Intruso", "Terror");

        bdLivros.close();
    }

    @Test
    public void consegueLerLivros() {
        Context appContext = getTargetContext();

        BdLivrosOpenHelper openHelper = new BdLivrosOpenHelper(appContext);
        SQLiteDatabase bdLivros = openHelper.getWritableDatabase();

        BdTableLivros tabelaLivros = new BdTableLivros(bdLivros);
        Cursor cursor = tabelaLivros.query(BdTableLivros.TODOS_CAMPOS, null, null, null, null, null);
        int registos = cursor.getCount();
        cursor.close();

        insereLivro(bdLivros, "O Intruso II", "Terror/Ação");

        cursor = tabelaLivros.query(BdTableLivros.TODOS_CAMPOS, null, null, null, null, null);
        assertEquals(registos + 1, cursor.getCount());
        cursor.close();

        bdLivros.close();
    }
}

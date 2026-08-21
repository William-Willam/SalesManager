import { useState } from 'react';
import toast from 'react-hot-toast';
import { Layout } from '../components/Layout';
import { ModalProduto } from '../components/ModalProduto';
import { Paginacao } from '../components/Paginacao';
import { useExcluirProduto, useProdutos } from '../hooks/useProdutos';
import { useAtualizarCategoria, useCategorias } from '../hooks/useCategorias';
import { SERVER_URL } from '../api/client';
import type { CategoriaResponse, ProdutoResponse } from '../api/types';

const ITENS_POR_PAGINA = 10;

function formatarMoeda(valor: number) {
  return valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
}

function PainelCategorias() {
  const { data: categorias } = useCategorias();
  const atualizarCategoria = useAtualizarCategoria();
  const [editandoId, setEditandoId] = useState<number | null>(null);
  const [nomeEditado, setNomeEditado] = useState('');

  function iniciarEdicao(categoria: CategoriaResponse) {
    setEditandoId(categoria.id);
    setNomeEditado(categoria.nome);
  }

  function salvar(id: number) {
    if (!nomeEditado.trim()) return;
    atualizarCategoria.mutate(
      { id, dados: { nome: nomeEditado.trim() } },
      {
        onSuccess: () => {
          toast.success('Categoria atualizada com sucesso!');
          setEditandoId(null);
        },
      }
    );
  }

  return (
    <div className="bg-white rounded-lg shadow-sm p-6 mb-6">
      <h3 className="text-slate-700 font-bold mb-4">Categorias</h3>
      <ul className="flex flex-wrap gap-3">
        {categorias?.map((categoria) => (
          <li key={categoria.id} className="flex items-center gap-2 bg-slate-50 rounded px-3 py-2">
            {editandoId === categoria.id ? (
              <>
                <input
                  value={nomeEditado}
                  onChange={(e) => setNomeEditado(e.target.value)}
                  className="border rounded px-2 py-1 text-sm outline-none w-32"
                  autoFocus
                />
                <button
                  onClick={() => salvar(categoria.id)}
                  className="text-orange-500 text-xs font-bold"
                >
                  Salvar
                </button>
                <button
                  onClick={() => setEditandoId(null)}
                  className="text-slate-400 text-xs"
                >
                  Cancelar
                </button>
              </>
            ) : (
              <>
                <span className="text-sm text-slate-700">{categoria.nome}</span>
                <button
                  onClick={() => iniciarEdicao(categoria)}
                  className="text-slate-400 hover:text-slate-600 text-xs font-bold"
                >
                  Editar
                </button>
              </>
            )}
          </li>
        ))}
      </ul>
    </div>
  );
}

export function Produtos() {
  const { data: produtos, isLoading } = useProdutos();
  const excluirProduto = useExcluirProduto();
  const [modalAberto, setModalAberto] = useState(false);
  const [produtoEditando, setProdutoEditando] = useState<ProdutoResponse | null>(null);
  const [pagina, setPagina] = useState(1);

  const totalPaginas = Math.max(1, Math.ceil((produtos?.length ?? 0) / ITENS_POR_PAGINA));
  const produtosDaPagina = produtos?.slice(
    (pagina - 1) * ITENS_POR_PAGINA,
    pagina * ITENS_POR_PAGINA
  );

  function aoExcluirClicar(id: number) {
    if (confirm('Tem certeza que deseja excluir este produto?')) {
      excluirProduto.mutate(id, {
        onSuccess: () => toast.success('Produto excluído com sucesso!'),
      });
    }
  }

  return (
    <Layout titulo="Produtos">
      <PainelCategorias />

      <div className="flex justify-end mb-4">
        <button
          onClick={() => setModalAberto(true)}
          className="bg-orange-500 hover:bg-orange-400 text-white font-bold px-4 py-2 rounded"
        >
          + Novo Produto
        </button>
      </div>

      <div className="bg-white rounded-lg overflow-hidden shadow-sm">
        <table className="w-full text-left">
          <thead className="bg-slate-50 text-slate-500 text-sm">
            <tr>
              <th className="px-6 py-3">Foto</th>
              <th className="px-6 py-3">Nome</th>
              <th className="px-6 py-3">Categoria</th>
              <th className="px-6 py-3">Preço</th>
              <th className="px-6 py-3"></th>
            </tr>
          </thead>
          <tbody>
            {isLoading && (
              <tr>
                <td colSpan={5} className="px-6 py-4 text-center text-slate-400">
                  Carregando...
                </td>
              </tr>
            )}
            {produtosDaPagina?.map((produto) => (
              <tr key={produto.id} className="border-t">
                <td className="px-6 py-3">
                  {produto.imagemUrl ? (
                    <img
                      src={`${SERVER_URL}${produto.imagemUrl}`}
                      alt={produto.nome}
                      className="w-10 h-10 rounded object-cover"
                    />
                  ) : (
                    <div className="w-10 h-10 rounded bg-slate-100" />
                  )}
                </td>
                <td className="px-6 py-3">{produto.nome}</td>
                <td className="px-6 py-3">{produto.categoria.nome}</td>
                <td className="px-6 py-3">{formatarMoeda(produto.preco)}</td>
                <td className="px-6 py-3 text-right space-x-3">
                  <button
                    onClick={() => setProdutoEditando(produto)}
                    className="text-slate-500 hover:text-slate-700 text-sm font-bold"
                  >
                    Editar
                  </button>
                  <button
                    onClick={() => aoExcluirClicar(produto.id)}
                    className="text-red-500 hover:text-red-600 text-sm font-bold"
                  >
                    Excluir
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <Paginacao paginaAtual={pagina} totalPaginas={totalPaginas} aoMudarPagina={setPagina} />

      {modalAberto && <ModalProduto aoFechar={() => setModalAberto(false)} />}
      {produtoEditando && (
        <ModalProduto produto={produtoEditando} aoFechar={() => setProdutoEditando(null)} />
      )}
    </Layout>
  );
}
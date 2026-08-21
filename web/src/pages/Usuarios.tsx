import { useState } from 'react';
import toast from 'react-hot-toast';
import { Layout } from '../components/Layout';
import { ModalNovoUsuario } from '../components/ModalNovoUsuario';
import { ModalEditarUsuario } from '../components/ModalEditarUsuario';
import { Paginacao } from '../components/Paginacao';
import { useExcluirUsuario, useUsuarios } from '../hooks/useUsuarios';
import type { UsuarioResponse } from '../api/types';

const ITENS_POR_PAGINA = 10;

export function Usuarios() {
  const { data: usuarios, isLoading } = useUsuarios();
  const excluirUsuario = useExcluirUsuario();
  const [modalAberto, setModalAberto] = useState(false);
  const [usuarioEditando, setUsuarioEditando] = useState<UsuarioResponse | null>(null);
  const [pagina, setPagina] = useState(1);

  const totalPaginas = Math.max(1, Math.ceil((usuarios?.length ?? 0) / ITENS_POR_PAGINA));
  const usuariosDaPagina = usuarios?.slice(
    (pagina - 1) * ITENS_POR_PAGINA,
    pagina * ITENS_POR_PAGINA
  );

  function aoExcluirClicar(id: number) {
    if (confirm('Tem certeza que deseja excluir este usuário?')) {
      excluirUsuario.mutate(id, {
        onSuccess: () => toast.success('Usuário excluído com sucesso!'),
      });
    }
  }

  return (
    <Layout titulo="Usuários">
      <div className="flex justify-end mb-4">
        <button
          onClick={() => setModalAberto(true)}
          className="bg-orange-500 hover:bg-orange-400 text-white font-bold px-4 py-2 rounded"
        >
          + Novo Usuário
        </button>
      </div>

      <div className="bg-white rounded-lg overflow-hidden shadow-sm">
        <table className="w-full text-left">
          <thead className="bg-slate-50 text-slate-500 text-sm">
            <tr>
              <th className="px-6 py-3">Nome</th>
              <th className="px-6 py-3">E-mail</th>
              <th className="px-6 py-3">Papel</th>
              <th className="px-6 py-3"></th>
            </tr>
          </thead>
          <tbody>
            {isLoading && (
              <tr>
                <td colSpan={4} className="px-6 py-4 text-center text-slate-400">
                  Carregando...
                </td>
              </tr>
            )}
            {usuariosDaPagina?.map((usuario) => (
              <tr key={usuario.id} className="border-t">
                <td className="px-6 py-3">{usuario.nome}</td>
                <td className="px-6 py-3">{usuario.email}</td>
                <td className="px-6 py-3">{usuario.papel}</td>
                <td className="px-6 py-3 text-right space-x-3">
                  <button
                    onClick={() => setUsuarioEditando(usuario)}
                    className="text-slate-500 hover:text-slate-700 text-sm font-bold"
                  >
                    Editar
                  </button>
                  <button
                    onClick={() => aoExcluirClicar(usuario.id)}
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

      {modalAberto && <ModalNovoUsuario aoFechar={() => setModalAberto(false)} />}
      {usuarioEditando && (
        <ModalEditarUsuario usuario={usuarioEditando} aoFechar={() => setUsuarioEditando(null)} />
      )}
    </Layout>
  );
}
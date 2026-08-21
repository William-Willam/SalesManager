import { useState, type FormEvent } from 'react';
import toast from 'react-hot-toast';
import { useAtualizarUsuario } from '../hooks/useUsuarios';
import { ApiException } from '../api/client';
import type { Papel, UsuarioResponse } from '../api/types';

export function ModalEditarUsuario({
  usuario,
  aoFechar,
}: {
  usuario: UsuarioResponse;
  aoFechar: () => void;
}) {
  const [nome, setNome] = useState(usuario.nome);
  const [email, setEmail] = useState(usuario.email);
  const [senha, setSenha] = useState('');
  const [confirmarSenha, setConfirmarSenha] = useState('');
  const [papel, setPapel] = useState<Papel>(usuario.papel);
  const [erro, setErro] = useState<string | null>(null);

  const atualizarUsuario = useAtualizarUsuario();

  function aoSubmeter(evento: FormEvent) {
    evento.preventDefault();
    setErro(null);

    if (senha && senha !== confirmarSenha) {
      setErro('As senhas não coincidem');
      return;
    }

    atualizarUsuario.mutate(
      { id: usuario.id, dados: { nome, email, senha, papel } },
      {
        onSuccess: () => {
          toast.success('Usuário atualizado com sucesso!');
          aoFechar();
        },
        onError: (erro) => {
          const mensagem = erro instanceof ApiException ? erro.message : 'Erro ao atualizar usuário';
          setErro(mensagem);
        },
      }
    );
  }

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center">
      <form
        onSubmit={aoSubmeter}
        className="bg-white rounded-lg p-8 w-full max-w-sm flex flex-col gap-4"
      >
        <h3 className="text-lg font-bold text-slate-800">Editar Usuário</h3>

        <div className="flex flex-col gap-1">
          <label className="text-sm text-slate-600">Nome</label>
          <input
            value={nome}
            onChange={(e) => setNome(e.target.value)}
            className="border rounded px-3 py-2 outline-none"
            required
          />
        </div>

        <div className="flex flex-col gap-1">
          <label className="text-sm text-slate-600">E-mail</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="border rounded px-3 py-2 outline-none"
            required
          />
        </div>

        <div className="flex flex-col gap-1">
          <label className="text-sm text-slate-600">
            Nova senha <span className="text-slate-400">(deixe em branco para manter a atual)</span>
          </label>
          <input
            type="password"
            value={senha}
            onChange={(e) => setSenha(e.target.value)}
            placeholder="••••••••"
            className="border rounded px-3 py-2 outline-none"
          />
        </div>

        {senha && (
          <div className="flex flex-col gap-1">
            <label className="text-sm text-slate-600">Confirmar nova senha</label>
            <input
              type="password"
              value={confirmarSenha}
              onChange={(e) => setConfirmarSenha(e.target.value)}
              className="border rounded px-3 py-2 outline-none"
            />
          </div>
        )}

        <div className="flex flex-col gap-1">
          <label className="text-sm text-slate-600">Papel</label>
          <select
            value={papel}
            onChange={(e) => setPapel(e.target.value as Papel)}
            className="border rounded px-3 py-2 outline-none"
          >
            <option value="ADMIN">Administrador</option>
            <option value="GERENTE">Gerente</option>
            <option value="ATENDENTE">Atendente</option>
          </select>
        </div>

        {erro && <p className="text-red-500 text-sm">{erro}</p>}

        <div className="flex gap-3 mt-2">
          <button
            type="button"
            onClick={aoFechar}
            className="flex-1 bg-slate-100 hover:bg-slate-200 text-slate-700 font-bold py-2 rounded"
          >
            Cancelar
          </button>
          <button
            type="submit"
            disabled={atualizarUsuario.isPending}
            className="flex-1 bg-orange-500 hover:bg-orange-400 disabled:opacity-50 text-white font-bold py-2 rounded"
          >
            {atualizarUsuario.isPending ? 'Salvando...' : 'Salvar'}
          </button>
        </div>
      </form>
    </div>
  );
}
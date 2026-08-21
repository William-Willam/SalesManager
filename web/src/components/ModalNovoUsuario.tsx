import { useState, type FormEvent } from 'react';
import toast from 'react-hot-toast';
import { useCriarUsuario } from '../hooks/useUsuarios';
import { ApiException } from '../api/client';
import type { Papel } from '../api/types';

export function ModalNovoUsuario({ aoFechar }: { aoFechar: () => void }) {
  const [nome, setNome] = useState('');
  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  const [confirmarSenha, setConfirmarSenha] = useState('');
  const [papel, setPapel] = useState<Papel>('ATENDENTE');
  const [erro, setErro] = useState<string | null>(null);

  const criarUsuario = useCriarUsuario();

  function aoSubmeter(evento: FormEvent) {
    evento.preventDefault();
    setErro(null);

    if (senha !== confirmarSenha) {
      setErro('As senhas não coincidem');
      return;
    }

    criarUsuario.mutate(
      { nome, email, senha, papel },
      {
        onSuccess: () => {
          toast.success('Usuário criado com sucesso!');
          aoFechar();
        },
        onError: (erro) => {
          const mensagem = erro instanceof ApiException ? erro.message : 'Erro ao criar usuário';
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
        <h3 className="text-lg font-bold text-slate-800">Novo Usuário</h3>

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
          <label className="text-sm text-slate-600">Senha</label>
          <input
            type="password"
            value={senha}
            onChange={(e) => setSenha(e.target.value)}
            className="border rounded px-3 py-2 outline-none"
            minLength={6}
            required
          />
        </div>

        <div className="flex flex-col gap-1">
          <label className="text-sm text-slate-600">Confirmar senha</label>
          <input
            type="password"
            value={confirmarSenha}
            onChange={(e) => setConfirmarSenha(e.target.value)}
            className="border rounded px-3 py-2 outline-none"
            required
          />
        </div>

        <div className="flex flex-col gap-1">
          <label className="text-sm text-slate-600">Papel</label>
          <select
            value={papel}
            onChange={(e) => setPapel(e.target.value as Papel)}
            className="border rounded px-3 py-2 outline-none"
          >
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
            disabled={criarUsuario.isPending}
            className="flex-1 bg-orange-500 hover:bg-orange-400 disabled:opacity-50 text-white font-bold py-2 rounded"
          >
            {criarUsuario.isPending ? 'Salvando...' : 'Salvar'}
          </button>
        </div>
      </form>
    </div>
  );
}
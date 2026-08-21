import { useState, type FormEvent } from "react";
import { useNavigate } from "react-router-dom";
import { useLogin } from "../hooks/useLogin";
import { useSessao } from "../context/SessaoContext";
import { ApiException } from "../api/client";

export function Login() {
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [erro, setErro] = useState<string | null>(null);

  const loginMutation = useLogin();
  const { login } = useSessao();
  const navegar = useNavigate();

  function aoSubmeter(evento: FormEvent) {
    evento.preventDefault();
    setErro(null);

    loginMutation.mutate(
      { email, senha },
      {
        onSuccess: (dados) => {
          if (dados.papel === "ATENDENTE") {
            setErro("Atendentes devem usar o aplicativo desktop.");
            return;
          }
          login(dados);
          navegar(dados.papel === "ADMIN" ? "/usuarios" : "/dashboard");
        },
        onError: (erro) => {
          const mensagem =
            erro instanceof ApiException
              ? erro.message
              : "Erro ao conectar com o servidor";
          setErro(mensagem);
        },
      },
    );
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-900">
      <form
        onSubmit={aoSubmeter}
        className="bg-slate-800 p-10 rounded-lg w-full max-w-sm flex flex-col gap-4"
      >
        <h1 className="text-white text-2xl font-bold text-center mb-4">
          SALES MANAGER
        </h1>

        <div className="flex flex-col gap-1">
          <label className="text-slate-400 text-sm">E-mail</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="rounded px-3 py-2 outline-none"
            required
          />
        </div>

        <div className="flex flex-col gap-1">
          <label className="text-slate-400 text-sm">Senha</label>
          <input
            type="password"
            value={senha}
            onChange={(e) => setSenha(e.target.value)}
            className="rounded px-3 py-2 outline-none"
            required
          />
        </div>

        {erro && <p className="text-red-400 text-sm">{erro}</p>}

        <button
          type="submit"
          disabled={loginMutation.isPending}
          className="bg-orange-500 hover:bg-orange-400 disabled:opacity-50 text-white font-bold py-2 rounded mt-2"
        >
          {loginMutation.isPending ? "Entrando..." : "ENTRAR"}
        </button>
      </form>
    </div>
  );
}

import type { ReactNode } from 'react';
import { useSessao } from '../context/SessaoContext';
import { useNavigate, useLocation, Link } from 'react-router-dom';

export function Layout({ titulo, children }: { titulo: string; children: ReactNode }) {
  const { usuario, logout } = useSessao();
  const navegar = useNavigate();
  const localizacao = useLocation();

  function aoSairClicar() {
    logout();
    navegar('/login');
  }

  const linkClasse = (rota: string) =>
    `text-sm font-bold px-3 py-1 rounded ${
      localizacao.pathname === rota
        ? 'bg-orange-500 text-white'
        : 'text-slate-300 hover:text-white'
    }`;

  return (
    <div className="min-h-screen bg-slate-100">
      <header className="bg-slate-900 px-8 py-4 flex items-center justify-between">
        <div className="flex items-center gap-6">
          <h1 className="text-white text-lg font-bold">Sales Manager</h1>
          {usuario?.papel === 'GERENTE' && (
            <nav className="flex gap-2">
              <Link to="/dashboard" className={linkClasse('/dashboard')}>Dashboard</Link>
              <Link to="/produtos" className={linkClasse('/produtos')}>Produtos</Link>
            </nav>
          )}
        </div>
        <div className="flex items-center gap-4">
          <span className="text-slate-300 text-sm">{usuario?.nome}</span>
          <button
            onClick={aoSairClicar}
            className="bg-slate-700 hover:bg-slate-600 text-slate-200 text-sm font-bold px-4 py-2 rounded"
          >
            Sair
          </button>
        </div>
      </header>
      <main className="p-8">
        <h2 className="text-2xl font-bold text-slate-800 mb-6">{titulo}</h2>
        {children}
      </main>
    </div>
  );
}
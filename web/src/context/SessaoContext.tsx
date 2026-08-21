import { createContext, useContext, useState, type ReactNode } from 'react';
import type { LoginResponse, Papel } from '../api/types';

interface SessaoContextType {
  usuario: LoginResponse | null;
  login: (dados: LoginResponse) => void;
  logout: () => void;
  temPapel: (...papeis: Papel[]) => boolean;
}

const SessaoContext = createContext<SessaoContextType | undefined>(undefined);

function usuarioSalvo(): LoginResponse | null {
  const bruto = localStorage.getItem('usuario');
  return bruto ? JSON.parse(bruto) : null;
}

export function SessaoProvider({ children }: { children: ReactNode }) {
  const [usuario, setUsuario] = useState<LoginResponse | null>(usuarioSalvo());

  function login(dados: LoginResponse) {
    localStorage.setItem('token', dados.token);
    localStorage.setItem('usuario', JSON.stringify(dados));
    setUsuario(dados);
  }

  function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('usuario');
    setUsuario(null);
  }

  function temPapel(...papeis: Papel[]) {
    return usuario !== null && papeis.includes(usuario.papel);
  }

  return (
    <SessaoContext.Provider value={{ usuario, login, logout, temPapel }}>
      {children}
    </SessaoContext.Provider>
  );
}

export function useSessao() {
  const contexto = useContext(SessaoContext);
  if (!contexto) {
    throw new Error('useSessao precisa ser usado dentro de um SessaoProvider');
  }
  return contexto;
}
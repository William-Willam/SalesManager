import type { ReactNode } from 'react';
import { Navigate } from 'react-router-dom';
import { useSessao } from '../context/SessaoContext';
import type { Papel } from '../api/types';

interface Props {
  papeisPermitidos: Papel[];
  children: ReactNode;
}

export function RotaProtegida({ papeisPermitidos, children }: Props) {
  const { usuario } = useSessao();

  if (!usuario) {
    return <Navigate to="/login" replace />;
  }

  if (!papeisPermitidos.includes(usuario.papel)) {
    return <Navigate to="/login" replace />;
  }

  return <>{children}</>;
}
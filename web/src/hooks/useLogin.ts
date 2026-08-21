import { useMutation } from '@tanstack/react-query';
import { apiClient } from '../api/client';
import type { LoginRequest, LoginResponse } from '../api/types';

export function useLogin() {
  return useMutation({
    mutationFn: async (dados: LoginRequest) => {
      const resposta = await apiClient.post<LoginResponse>('/login', dados);
      return resposta.data;
    },
  });
}
import { useQuery } from '@tanstack/react-query';
import { apiClient } from '../api/client';
import type { DashboardResponse } from '../api/types';

export function useDashboard(dias: number) {
  return useQuery({
    queryKey: ['dashboard', dias],
    queryFn: async () => {
      const resposta = await apiClient.get<DashboardResponse>(`/dashboard?dias=${dias}`);
      return resposta.data;
    },
  });
}
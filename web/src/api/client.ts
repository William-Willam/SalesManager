import axios, { type AxiosInstance, AxiosError } from 'axios';
import type { ErroResposta } from './types';

export const SERVER_URL = 'http://localhost:8080';
const BASE_URL = `${SERVER_URL}/api`;

export class ApiException extends Error {
  status: number;

  constructor(status: number, mensagem: string) {
    super(mensagem);
    this.status = status;
  }
}

function criarApiClient(): AxiosInstance {
  const instancia = axios.create({ baseURL: BASE_URL });

  instancia.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  });

  instancia.interceptors.response.use(
    (response) => response,
    (error: AxiosError<ErroResposta>) => {
      const status = error.response?.status ?? 0;
      const mensagem =
        error.response?.data?.mensagem ?? 'Erro na comunicação com o servidor';
      return Promise.reject(new ApiException(status, mensagem));
    }
  );

  return instancia;
}

export const apiClient = criarApiClient();
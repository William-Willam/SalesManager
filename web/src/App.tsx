import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import { SessaoProvider } from './context/SessaoContext';
import { RotaProtegida } from './routes/RotaProtegida';
import { Login } from './pages/login';
import { Usuarios } from './pages/Usuarios';
import { Dashboard } from './pages/Dashboard';
import { Produtos } from './pages/Produtos';

const queryClient = new QueryClient();

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <Toaster position="top-right" toastOptions={{ duration: 3000 }} />
      <SessaoProvider>
        <BrowserRouter>
          <Routes>
            <Route path="/login" element={<Login />} />
            <Route
              path="/usuarios"
              element={
                <RotaProtegida papeisPermitidos={['ADMIN']}>
                  <Usuarios />
                </RotaProtegida>
              }
            />
            <Route
              path="/dashboard"
              element={
                <RotaProtegida papeisPermitidos={['GERENTE']}>
                  <Dashboard />
                </RotaProtegida>
              }
            />
            <Route
              path="/produtos"
              element={
                <RotaProtegida papeisPermitidos={['GERENTE']}>
                  <Produtos />
                </RotaProtegida>
              }
            />
            <Route path="*" element={<Navigate to="/login" replace />} />
          </Routes>
        </BrowserRouter>
      </SessaoProvider>
    </QueryClientProvider>
  );
}

export default App;
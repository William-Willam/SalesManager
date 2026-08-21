import { useState } from "react";
import {
  Bar,
  BarChart,
  CartesianGrid,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from "recharts";
import { Layout } from "../components/Layout";
import { CardIndicador } from "../components/CardIndicador";
import { useDashboard } from "../hooks/useDashboard";

function formatarMoeda(valor: number) {
  return valor.toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
}

function formatarDataCurta(data: string) {
  const [, mes, dia] = data.split("-");
  return `${dia}/${mes}`;
}

export function Dashboard() {
  const [dias, setDias] = useState(30);
  const { data, isLoading } = useDashboard(dias);

  return (
    <Layout titulo="Dashboard">
      <div className="flex justify-end mb-6 gap-2">
        {[7, 30, 90].map((opcao) => (
          <button
            key={opcao}
            onClick={() => setDias(opcao)}
            className={`px-4 py-2 rounded text-sm font-bold ${
              dias === opcao
                ? "bg-orange-500 text-white"
                : "bg-white text-slate-600 hover:bg-slate-100"
            }`}
          >
            {opcao} dias
          </button>
        ))}
      </div>

      {isLoading && <p className="text-slate-400">Carregando...</p>}

      {data && (
        <>
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 mb-8">
            <CardIndicador
              titulo="Total de vendas"
              valor={formatarMoeda(data.totalVendas)}
            />
            <CardIndicador
              titulo="Quantidade de vendas"
              valor={String(data.quantidadeVendas)}
            />
          </div>

          <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
            <div className="lg:col-span-2 bg-white rounded-lg p-6 shadow-sm">
              <h3 className="text-slate-700 font-bold mb-4">
                Vendas por período
              </h3>
              <ResponsiveContainer width="100%" height={280}>
                <BarChart data={data.vendasPorDia}>
                  <CartesianGrid strokeDasharray="3 3" vertical={false} />
                  <XAxis
                    dataKey="data"
                    tickFormatter={formatarDataCurta}
                    fontSize={12}
                    stroke="#94a3b8"
                  />
                  <YAxis fontSize={12} stroke="#94a3b8" />
                  <Tooltip
                    formatter={(valor) => formatarMoeda(Number(valor))}
                    labelFormatter={(label) => formatarDataCurta(String(label))}
                  />
                  <Bar dataKey="total" fill="#f97316" radius={[4, 4, 0, 0]} />
                </BarChart>
              </ResponsiveContainer>
            </div>

            <div className="bg-white rounded-lg p-6 shadow-sm">
              <h3 className="text-slate-700 font-bold mb-4">
                Produtos mais vendidos
              </h3>
              <ul className="flex flex-col gap-3">
                {data.produtosMaisVendidos.map((produto, indice) => (
                  <li
                    key={produto.nome}
                    className="flex items-center justify-between"
                  >
                    <span className="text-slate-600 text-sm">
                      {indice + 1}. {produto.nome}
                    </span>
                    <span className="text-slate-800 font-bold text-sm">
                      {produto.quantidadeVendida}x
                    </span>
                  </li>
                ))}
                {data.produtosMaisVendidos.length === 0 && (
                  <p className="text-slate-400 text-sm">
                    Nenhuma venda no período
                  </p>
                )}
              </ul>
            </div>
          </div>
        </>
      )}
    </Layout>
  );
}

import { BrowserRouter, Routes, Route } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { Layout } from "./components/layout/Layout";
import { Dashboard } from "./pages/Dashboard/Dashboard";
import { ItemsPage } from "./pages/Items/ItemsPage";
import ItemDetailsPage from "./pages/Items/ItemDetailsPage";
import { CategoriesPage } from "./pages/Categories/CategoriesPage";
import { TemplatesPage } from "./pages/Templates/TemplatesPage";
import { TemplateDetailsPage } from "./pages/Templates/TemplateDetailsPage";
import { TemplateFormPage } from "./pages/Templates/TemplateFormPage";
import { ToastProvider } from "./components/ui/Toast";

// Create a client
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 1000 * 60 * 5, // 5 minutes
      refetchOnWindowFocus: false,
      retry: 1,
    },
  },
});

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <ToastProvider>
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<Layout />}>
              <Route index element={<Dashboard />} />
              <Route path="items" element={<ItemsPage />} />
              <Route path="items/:id" element={<ItemDetailsPage />} />
              <Route path="templates" element={<TemplatesPage />} />
              <Route path="templates/new" element={<TemplateFormPage />} />
              <Route path="templates/:id" element={<TemplateDetailsPage />} />
              <Route
                path="templates/:id/edit"
                element={<TemplateFormPage key="template-edit" />}
              />
              <Route
                path="events"
                element={
                  <div className="text-center py-12">
                    <h2 className="text-2xl font-bold text-gray-900">Eventos</h2>
                    <p className="text-gray-600 mt-2">Em desenvolvimento...</p>
                  </div>
                }
              />
              <Route
                path="alerts"
                element={
                  <div className="text-center py-12">
                    <h2 className="text-2xl font-bold text-gray-900">Alertas</h2>
                    <p className="text-gray-600 mt-2">Em desenvolvimento...</p>
                  </div>
                }
              />
              <Route path="categories" element={<CategoriesPage />} />
            </Route>
          </Routes>
        </BrowserRouter>
      </ToastProvider>
    </QueryClientProvider>
  );
}

export default App;

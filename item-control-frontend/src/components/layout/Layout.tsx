import { Link, Outlet, useLocation } from "react-router-dom";
import {
  LayoutDashboard,
  Package,
  Calendar,
  Bell,
  FolderTree,
  Settings,
  Menu,
  X,
} from "lucide-react";
import { useState } from "react";
import { ApiSettingsModal } from "../ApiSettingsModal";

const navigation = [
  { name: "Dashboard", href: "/", icon: LayoutDashboard },
  { name: "Items", href: "/items", icon: Package },
  { name: "Eventos", href: "/events", icon: Calendar },
  { name: "Alertas", href: "/alerts", icon: Bell },
  { name: "Categorias", href: "/categories", icon: FolderTree },
];

export function Layout() {
  const location = useLocation();
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [apiSettingsOpen, setApiSettingsOpen] = useState(false);

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white border-b border-gray-200 sticky top-0 z-10">
        <div className="px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            {/* Mobile menu button */}
            <button
              onClick={() => setSidebarOpen(!sidebarOpen)}
              className="lg:hidden p-2 rounded-md text-gray-600 hover:bg-gray-100"
            >
              {sidebarOpen ? (
                <X className="h-6 w-6" />
              ) : (
                <Menu className="h-6 w-6" />
              )}
            </button>

            {/* Logo */}
            <div className="flex items-center">
              <Link to="/" className="flex items-center space-x-2">
                <Package className="h-8 w-8 text-primary-600" />
                <span className="text-xl font-bold text-gray-900">
                  Item Control
                </span>
              </Link>
            </div>

            {/* Right menu */}
            <div className="flex items-center space-x-4">
              <Link
                to="/alerts"
                className="relative p-2 text-gray-600 hover:text-gray-900 hover:bg-gray-100 rounded-lg"
              >
                <Bell className="h-5 w-5" />
                <span className="absolute top-1 right-1 block h-2 w-2 rounded-full bg-red-500"></span>
              </Link>
              <button
                className="p-2 text-gray-600 hover:text-gray-900 hover:bg-gray-100 rounded-lg"
                onClick={() => setApiSettingsOpen(true)}
              >
                <Settings className="h-5 w-5" />
              </button>
            </div>
          </div>
        </div>
      </header>

      <ApiSettingsModal
        isOpen={apiSettingsOpen}
        onClose={() => setApiSettingsOpen(false)}
      />

      <div className="flex">
        {/* Sidebar */}
        <aside
          className={`
            ${sidebarOpen ? "translate-x-0" : "-translate-x-full"}
            lg:translate-x-0
            fixed lg:static inset-y-0 left-0 z-20
            w-64 bg-white border-r border-gray-200
            transition-transform duration-300 ease-in-out
            mt-16 lg:mt-0
          `}
        >
          <nav className="h-full px-4 py-6 space-y-1">
            {navigation.map((item) => {
              const Icon = item.icon;
              const isActive = location.pathname === item.href;

              return (
                <Link
                  key={item.name}
                  to={item.href}
                  onClick={() => setSidebarOpen(false)}
                  className={`
                    flex items-center space-x-3 px-3 py-2 rounded-lg text-sm font-medium
                    transition-colors
                    ${
                      isActive
                        ? "bg-primary-50 text-primary-700"
                        : "text-gray-700 hover:bg-gray-100 hover:text-gray-900"
                    }
                  `}
                >
                  <Icon className="h-5 w-5" />
                  <span>{item.name}</span>
                </Link>
              );
            })}
          </nav>
        </aside>

        {/* Main content */}
        <main className="flex-1 min-h-[calc(100vh-4rem)]">
          <div className="px-4 sm:px-6 lg:px-8 py-8">
            <Outlet />
          </div>
        </main>
      </div>

      {/* Overlay for mobile sidebar */}
      {sidebarOpen && (
        <div
          className="fixed inset-0 bg-black bg-opacity-50 z-10 lg:hidden mt-16"
          onClick={() => setSidebarOpen(false)}
        ></div>
      )}
    </div>
  );
}

import User from 'src/types/user/user.ts';
import { createContext, ReactNode, useContext, useEffect, useState } from 'react';
import toast from 'react-hot-toast';
import CONSTANTS from 'src/constants.ts';

interface AuthContextType {
  user: User | null;
  setUser: (user: User | null) => void;
  isLoggedIn: boolean;
  login: (user: User) => void;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [user, setUser] = useState<User | null>(null);
  const isLoggedIn = user !== null;

  const login = async (user: User) => {
    setUser(user);
    localStorage.setItem('userId', user.id);
    localStorage.setItem('username', user.username);
    localStorage.setItem('postalCode', user.postalCode);
  }

  const logout = async () => {
    try {
      const res = await fetch(CONSTANTS.backendEndpoint + '/auth/logout', {
        method: 'POST',
        credentials: 'include',
      })

      if (!res.ok) throw new Error("Logout failed");

      setUser(null);
      localStorage.removeItem('userId');
      localStorage.removeItem('username');
      localStorage.removeItem('postalCode');

      toast.success('Logout successful');

    } catch (err) {
      console.log(err);
      toast.error("Failed to logout");
    }

  }

  useEffect(() => {
    const storedUserId = localStorage.getItem('userId');
    const storedUsername = localStorage.getItem('username');
    const storedPostalCode = localStorage.getItem('postalCode');
    if (storedUserId && storedUsername && storedPostalCode) {
      try {
        setUser({
          id: storedUserId,
          username: storedUsername,
          postalCode: storedPostalCode
        })
      } catch (err) {
        console.error("Failed to parse User Info from localStorage", err);
        localStorage.removeItem('userId');
        localStorage.removeItem('username');
        localStorage.removeItem('postalCode');
      }
    }
  }, []);

  return (
    <AuthContext.Provider value={{ user, setUser, isLoggedIn, login, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}
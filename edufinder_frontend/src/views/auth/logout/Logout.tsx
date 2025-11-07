import { useAuth } from 'src/context/AuthProvider.tsx';
import { useNavigate } from 'react-router';
import { useEffect } from 'react';

const Logout = () => {
  const { logout } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    logout();
    navigate("/");
  }, []);

  return null;
}

export default Logout;
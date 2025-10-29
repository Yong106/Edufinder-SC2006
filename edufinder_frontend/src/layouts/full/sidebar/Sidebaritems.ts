export interface ChildItem {
  id?: number | string;
  name?: string;
  icon?: any;
  children?: ChildItem[];
  item?: any;
  url?: any;
  color?: string;
  isPro?: boolean;
}

export interface MenuItem {
  heading?: string;
  name?: string;
  icon?: any;
  id?: number;
  to?: string;
  items?: MenuItem[];
  children?: ChildItem[];
  url?: any;
  isPro?: boolean;
}

import { uniqueId } from 'lodash';

const SidebarContent: MenuItem[] = [
  {
    children: [
      {
        name: 'Home',
        icon: 'solar:home-2-outline',
        id: uniqueId(),
        url: '/',
        isPro: false,
      },
      {
        name: 'Saved Schools',
        icon: 'solar:bookmark-linear',
        id: uniqueId(),
        url: '/savedschools',
        isPro: false,
      },
      {
        name: 'Compare Schools',
        icon: 'solar:chart-line-duotone',
        id: uniqueId(),
        url: '/compareschools',
        isPro: false,
      }
    ],
  },
  {
    heading: 'Auth',
    children: [
      {
        name: 'Login',
        icon: 'solar:login-2-linear',
        id: uniqueId(),
        url: '/auth/login',
        isPro: false,
      },
      {
        name: 'Register',
        icon: 'solar:shield-user-outline',
        id: uniqueId(),
        url: '/auth/register',
        isPro: false,
      }
    ],
  },
];

export default SidebarContent;

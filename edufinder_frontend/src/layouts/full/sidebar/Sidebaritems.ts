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
    heading: 'Utilities',
    children: [
      {
        name: 'Buttons',
        icon: 'solar:palette-round-line-duotone',
        id: uniqueId(),
        url: '/ui/buttons',
        isPro: false,
      },
      {
        name: 'Typography',
        icon: 'solar:text-circle-outline',
        id: uniqueId(),
        url: '/ui/typography',
        isPro: false,
      },
      {
        name: 'Table',
        icon: 'solar:bedside-table-3-linear',
        id: uniqueId(),
        url: '/ui/table',
        isPro: false,
      },
      {
        name: 'Form',
        icon: 'solar:password-minimalistic-outline',
        id: uniqueId(),
        url: '/ui/form',
        isPro: false,
      },
      {
        name: 'Alert',
        icon: 'solar:airbuds-case-charge-outline',
        id: uniqueId(),
        url: '/ui/alert',
        isPro: false,
      },
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

// @ts-check
import { themes as prismThemes } from 'prism-react-renderer';

/** @type {import('@docusaurus/types').Config} */
const config = {
  title: 'BreakdownKernel',
  tagline: '瓦解核心 — 面向 26.1.2 的大型系列 Mod 文档',
  favicon: 'img/favicon.ico',

  future: {
    v4: true,
  },

  url: 'https://PhasetransCrystal.github.io',
  baseUrl: process.env.DOCUSAURUS_BASE_URL ?? '/',

  organizationName: 'PhasetransCrystal',
  projectName: 'BreakdownKernel',
  trailingSlash: false,

  onBrokenLinks: 'throw',

  markdown: {
    hooks: {
      onBrokenMarkdownLinks: 'warn',
    },
  },

  i18n: {
    defaultLocale: 'en',
    locales: ['en'],
  },

  presets: [
    [
      'classic',
      /** @type {import('@docusaurus/preset-classic').Options} */
      ({
        docs: {
          path: '../docs',
          routeBasePath: '/',
          sidebarPath: './sidebars.js',
          editUrl:
            'https://github.com/PhasetransCrystal/BreakdownKernel/tree/master/docs/',
        },
        blog: false,
        theme: {
          customCss: './src/css/custom.css',
        },
      }),
    ],
  ],

  themeConfig:
    /** @type {import('@docusaurus/preset-classic').ThemeConfig} */
    ({
      colorMode: {
        defaultMode: 'light',
        respectPrefersColorScheme: true,
      },
      docs: {
        sidebar: {
          hideable: true,
          autoCollapseCategories: true,
        },
      },
      navbar: {
        title: 'BreakdownKernel',
        items: [
          {
            type: 'docSidebar',
            sidebarId: 'mainSidebar',
            position: 'left',
            label: 'Docs',
          },
          {
            href: 'https://github.com/PhasetransCrystal/BreakdownKernel',
            label: 'GitHub',
            position: 'right',
          },
        ],
      },
      footer: {
        style: 'light',
        links: [
          {
            title: '文档',
            items: [
              { label: '总览', to: '/' },
              { label: '通用材料系统', to: '/material' },
            ],
          },
          {
            title: '更多',
            items: [
              {
                label: 'GitHub',
                href: 'https://github.com/PhasetransCrystal/BreakdownKernel',
              },
            ],
          },
        ],
        copyright: `Copyright (c) ${new Date().getFullYear()} PhasetransCrystal. Built with Docusaurus.`,
      },
      prism: {
        theme: prismThemes.oneLight,
        darkTheme: prismThemes.oneDark,
        additionalLanguages: ['java', 'groovy', 'gradle', 'toml'],
      },
    }),

  plugins: [
    [
      '@docusaurus/plugin-client-redirects',
      {
      },
    ],
  ],
};

export default config;
import { MenuItem } from "./Models/MenuItem/MenuItem";

export const MENU_CONFIG: Record<string, MenuItem[]> = {
  ROLE_MANAGER: [
    {
      id: 'home',
      label: 'Inicio',
      route: '/'
    },
    {
      id: 'users',
      label: 'Usuarios',
      subItems: [
        { id: 'students', label: 'Alumnos', route: '/students' },
        { id: 'teachers', label: 'Docentes', route: '/teachers' },
        { id: 'managers', label: 'Gestores', route: '/managers' }
      ]
    },
    {
      id: 'academic',
      label: 'Académico',
      subItems: [
        { id: 'subjects', label: 'Materias', route: '/subject' },
        { id: 'exams', label: 'Exámenes', route: '/exams' },
        { id: 'commissions', label: 'Comisiones', route: '/commissions' }
      ]
    },
    {
      id: 'offers',
      label: 'Ofertas académicas',
      subItems: [
        { id: 'engineerings', label: 'Ingenierías', route: '/engineerings' },
        { id: 'technicals', label: 'Tecnicaturas', route: '/technicals' },
        { id: 'courses', label: 'Cursos', route: '/courses' }
      ]
    },
    {
      id: 'inscriptions',
      label: 'Inscripciones',
      subItems: [
        { id: 'inscriptions-home', label: 'Inscripciones', route: '/select-type-register' },
        { id: 'create-final-inscription', label: 'Crear Inscripción a mesa de examen', route: '/inscriptionToFinalExam' }
      ]
    }
  ],

  ROLE_TEACHER: [
    {
      id: 'home',
      label: 'Inicio',
      route: '/'
    }
  ],

  ROLE_STUDENT: [
    {
      id: 'home',
      label: 'Inicio',
      route: '/'
    },
    {
      id: 'student-inscriptions',
      label: 'Inscripciones',
      subItems: [
        {
          id: 'student-final-inscription',
          label: 'Inscribirme a final',
          route: '/register-student-to-final-exam'
        }
      ]
    }
  ]
};

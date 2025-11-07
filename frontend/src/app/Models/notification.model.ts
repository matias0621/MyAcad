export type NotificationType = 'success' | 'error' | 'info' | 'warning';
export type ModalType = 'error' | 'warning' | 'confirm';

export interface Notification {
  id: number;
  message: string;
  type: NotificationType;
  duration?: number;
}

export interface Modal {
  message: string;
  type: ModalType;
  title?: string;
  confirmText?: string;
  cancelText?: string;
}

export interface MenuItem {
  id: string;               
  label: string;      
  route?: string;      
  subItems?: MenuItem[];   
}

import { Component, OnInit } from '@angular/core';
import { CourseEvaluationService } from '../../Services/CourseEvaluation/course-evaluation-service';
import { CourseEvaluation } from '../../Models/CourseEvaluation/CourseEvaluation';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-surveys',
  imports: [DatePipe],
  templateUrl: './surveys.html',
  styleUrl: './surveys.css'
})
export class Surveys implements OnInit {
  surveysList: CourseEvaluation[] = [];
  loading: boolean = true;
  currentPage: number = 0;
  totalPages: number = 0;
  pageSize: number = 10;
  expandedSurveys: Set<number> = new Set();

  constructor(
    private courseEvaluationService: CourseEvaluationService
  ) {}

  ngOnInit(): void {
    this.loadSurveys();
  }

  loadSurveys() {
    this.loading = true;
    this.courseEvaluationService.getAllEvaluation(this.currentPage, this.pageSize).subscribe({
      next: (response: any) => {
        if (response.content) {
          this.surveysList = response.content;
          this.totalPages = response.totalPages || 0;
          this.currentPage = response.number || 0;
        } else if (Array.isArray(response)) {
          this.surveysList = response;
          this.totalPages = 1;
          this.currentPage = 0;
        } else {
          this.surveysList = [];
        }
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading surveys:', err);
        this.loading = false;
        this.surveysList = [];
      }
    });
  }

  parseFeedback(feedback: string): any {
    if (!feedback) return { teacher: '', content: '', suggestion: '' };
    
    const parts = feedback.split('\n\n');
    const result: any = { teacher: '', content: '', suggestion: '' };
    
    parts.forEach(part => {
      if (part.startsWith('DOCENTE:')) {
        result.teacher = part.replace('DOCENTE:', '').trim();
      } else if (part.startsWith('CONTENIDOS:')) {
        result.content = part.replace('CONTENIDOS:', '').trim();
      } else if (part.startsWith('SUGERENCIAS:')) {
        result.suggestion = part.replace('SUGERENCIAS:', '').trim();
      }
    });
    
    return result;
  }

  nextPage() {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.loadSurveys();
    }
  }

  previousPage() {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.loadSurveys();
    }
  }

  toggleSurvey(surveyId: number) {
    if (this.expandedSurveys.has(surveyId)) {
      this.expandedSurveys.delete(surveyId);
    } else {
      this.expandedSurveys.add(surveyId);
    }
  }

  isExpanded(surveyId: number): boolean {
    return this.expandedSurveys.has(surveyId);
  }
}


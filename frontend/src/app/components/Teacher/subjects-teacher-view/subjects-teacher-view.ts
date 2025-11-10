import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProgramService } from '../../../Services/program-service';

@Component({
  selector: 'app-subjects-teacher-view',
  imports: [],
  templateUrl: './subjects-teacher-view.html',
  styleUrl: './subjects-teacher-view.css'
})
export class SubjectsTeacherView implements OnInit{
  programId!: number;

  constructor(
    private route : ActivatedRoute,
    private pService : ProgramService
  ){

  }

ngOnInit(): void {
  this.programId = Number(this.route.snapshot.params['programId'])
}
}

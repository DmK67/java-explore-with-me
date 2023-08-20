package ru.practicum.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class CompilationController {
    private final CompilationService compilationService;

    @GetMapping("/compilations")
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilationById(@PathVariable @Valid @Positive Long compId) {
        return compilationService.getCompilationById(compId);
    }

    @PostMapping("/admin/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        return compilationService.createCompilation(newCompilationDto);
    }

    @PatchMapping("/admin/compilations/{compId}")
    public CompilationDto updateCompilation(@PathVariable @Valid @Positive Long compId,
                                            @RequestBody @Valid UpdateCompilationRequestDto updateCompilationRequestDto) {
        return compilationService.updateCompilation(compId, updateCompilationRequestDto);
    }

    @DeleteMapping(value = "/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        compilationService.deleteCompilation(compId);
    }
}

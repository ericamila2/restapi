package dio.restapi2.controller;

import dio.restapi2.controller.dto.HolderDto;
import dio.restapi2.service.HolderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/holders")
@Tag(name = "Holders Controller", description = "RESTFULL API for managing holders.")
public record HolderController(HolderService holderService) {

    @GetMapping
    @Operation(summary = "Get all holders", description = "Retrieve a list of all registered holders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful")
    })
    public ResponseEntity<List<HolderDto>> findAll() {
        var holders = holderService.findAll();
        var holdersDto = holders.stream().map(HolderDto::new).collect(Collectors.toList());
        return ResponseEntity.ok(holdersDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a holder by ID", description = "Retrieve a specific holder based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful"),
            @ApiResponse(responseCode = "404", description = "Holder not found")
    })
    public ResponseEntity<HolderDto> findById(@PathVariable Long id) {
        var holder = holderService.findById(id);
        return ResponseEntity.ok(new HolderDto(holder));
    }

    @PostMapping
    @Operation(summary = "Create a new holder", description = "Create a new holder and return the created holder's data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Holder created successfully"),
            @ApiResponse(responseCode = "422", description = "Invalid holder data provided")
    })
    public ResponseEntity<HolderDto> create(@RequestBody HolderDto holderDto) {
        var holder = holderService.create(holderDto.toModel());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(holder.getId())
                .toUri();
        return ResponseEntity.created(location).body(new HolderDto(holder));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a holder", description = "Update the data of an existing holder based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Holder updated successfully"),
            @ApiResponse(responseCode = "404", description = "Holder not found"),
            @ApiResponse(responseCode = "422", description = "Invalid holder data provided")
    })
    public ResponseEntity<HolderDto> update(@PathVariable Long id, @RequestBody HolderDto holderDto) {
        var holder = holderService.update(id, holderDto.toModel());
        return ResponseEntity.ok(new HolderDto(holder));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a holder", description = "Delete an existing holder based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Holder deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Holder not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        holderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
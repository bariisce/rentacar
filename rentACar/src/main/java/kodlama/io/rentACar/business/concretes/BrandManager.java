package kodlama.io.rentACar.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import kodlama.io.rentACar.business.abstracts.BrandService;
import kodlama.io.rentACar.business.requests.CreateBrandRequest;
import kodlama.io.rentACar.business.requests.UpdateBrandRequest;
import kodlama.io.rentACar.business.responses.GetAllBrandsResponses;
import kodlama.io.rentACar.business.responses.GetByIdBrandsResponse;
import kodlama.io.rentACar.business.rules.BrandBusinessRules;
import kodlama.io.rentACar.core.utilities.mappers.ModelMapperService;
import kodlama.io.rentACar.dataAccess.abstracts.BrandRepository;
import kodlama.io.rentACar.entities.concretes.Brand;
import lombok.AllArgsConstructor;

@Service // bu sınıf bir business nesnesidir demek.
@AllArgsConstructor
public class BrandManager implements BrandService{
	private BrandRepository brandRepository;
	private ModelMapperService modelMapperService;
	private BrandBusinessRules brandBusinessRules;
	
	@Override
	public List<GetAllBrandsResponses> getAll() {
		// iş kuralları 
		List<Brand> brands = brandRepository.findAll();
		
		List<GetAllBrandsResponses> brandsResponse = brands.stream()
				.map(brand->this.modelMapperService.forResponse()
				.map(brand, GetAllBrandsResponses.class)).collect(Collectors.toList());
		return brandsResponse;
	}


	@Override
	public void add(CreateBrandRequest createBrandRequest) {
		this.brandBusinessRules.checkIfBrandNameExists(createBrandRequest.getName());
		
		Brand brand = this.modelMapperService.forRequest().map(createBrandRequest, Brand.class);
		this.brandRepository.save(brand);
		
	}


	@Override
	public GetByIdBrandsResponse getById(int id) {
		Brand brand = this.brandRepository.findById(id).orElseThrow();
		GetByIdBrandsResponse brandsResponse = this.modelMapperService.forResponse()
				.map(brand, GetByIdBrandsResponse.class);
		return brandsResponse;
	}


	@Override
	public void update(UpdateBrandRequest updateBrandRequest) {
		Brand brand = this.modelMapperService.forRequest().map(updateBrandRequest, Brand.class);
		this.brandRepository.save(brand);
		
	}


	@Override
	public void delete(int id) {
		this.brandRepository.deleteById(id);
		
	}

}

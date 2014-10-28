<?php
namespace NGS\Patterns;

require_once(__DIR__.'/../Converter/PrimitiveConverter.php');
require_once(__DIR__.'/../Client/DomainProxy.php');

use \NGS\Client\DomainProxy;
use \NGS\Converter\PrimitiveConverter;
use NGS\Name;

class SearchBuilder extends Search
{
    private $specification;

    public function __construct(Specification $specification)
    {
        $this->specification = $specification;
    }

    public function __get($name)
    {
        return $this->specification->{$name};
    }

    public function __set($name, $value)
    {
        $this->specification->{$name} = $value;
    }

    public function search()
    {
        $class = get_class($this->specification);
        $target = substr($class, 0, strrpos($class, '\\'));
        return
            DomainProxy::instance()->searchWithSpecification(
                $target,
                $this->specification,
                $this->limit,
                $this->offset,
                $this->order);
    }
}
